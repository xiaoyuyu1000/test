package cn.appsys.controller;

import cn.appsys.pojo.*;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppVersionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.util.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/dev/flatform/app")
public class AppController {
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppCategoryService appCategoryService;
    @Autowired
    private AppVersionService appVersionService;
    @RequestMapping("/list")
    public String getAppInfoList(@RequestParam(value = "querySoftwareName",required = false) String querySoftwareName,
                                 @RequestParam(value = "queryStatus",required = false) String _queryStatus,
                                 @RequestParam(value = "queryFlatformId",required = false)String _queryFlatformId,
                                 @RequestParam(value = "queryCategoryLevel1" ,required = false)String _queryCategoryLevel1,
                                 @RequestParam(value = "queryCategoryLevel2" ,required = false)String _queryCategoryLevel2,
                                 @RequestParam(value = "queryCategoryLevel3" ,required = false)String _queryCategoryLevel3,
                                 @RequestParam(value="pageIndex",required=false) String pageIndex,
                                 Model model, HttpSession session){
        Integer devId=((DevUser)session.getAttribute("devUserSession")).getId();
        System.out.println("id"+devId);
        List<AppInfo> appInfoList=null;
        List<DataDictionary> statusList=null;//状态集合
        List<DataDictionary> flatFormList=null;//平台集合
        List<AppCategory> categoryLevel1List=null;
        List<AppCategory> categoryLevel2List=null;
        List<AppCategory> categoryLevel3List=null;
        //页面容量
        int pageSize=5;
        //当前页码
        Integer currentPageNo=1;
        if (pageIndex!=null){
            try {
                currentPageNo=Integer.valueOf(pageIndex);

            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        System.out.println("当前页码"+currentPageNo);
        Integer queryStatus=null;
        if (_queryStatus!=null&& !_queryStatus.equals("")){
            queryStatus=Integer.parseInt(_queryStatus);
        }
        Integer queryCategoryLevel1 = null;
        if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
            queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
        }
        Integer queryCategoryLevel2 = null;
        if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
            queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
        }
        Integer queryCategoryLevel3 = null;
        if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
            queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
        }
        Integer queryFlatformId = null;
        if(_queryFlatformId != null && !_queryFlatformId.equals("")){
            queryFlatformId = Integer.parseInt(_queryFlatformId);
        }

        //总数量
        int totalCount=0;
        try {
            totalCount=appInfoService.getAppInfoCount(querySoftwareName,queryStatus,
                    queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId);
        }catch (Exception e){
            e.printStackTrace();
        }
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        System.out.println("总记录数"+totalCount);
        int totalPageCount=pages.getTotalPageCount();
        System.out.println("总页数"+totalPageCount);
        //控制首页和尾页
        if (currentPageNo<1){
            currentPageNo=1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }
        System.out.println("currentPageNo"+currentPageNo);
        try{
            appInfoList=appInfoService.getAppInfoList(querySoftwareName,queryStatus,
                    queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,devId,currentPageNo,pageSize);
            statusList=this.getDataDictionaryList("APP_STATUS");
            flatFormList=this.getDataDictionaryList("APP_FLATFORM");
            categoryLevel1List=appCategoryService.getAppCategoryListByParentId(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(AppInfo appInfo:appInfoList){
            System.out.println(appInfo);
        }
        System.out.println("长度"+appInfoList.size());
        model.addAttribute("appInfoList",appInfoList);
        model.addAttribute("statusList",statusList);
        model.addAttribute("flatFormList",flatFormList);
        model.addAttribute("categoryLevel1List",categoryLevel1List);
        model.addAttribute("pages", pages);
        model.addAttribute("queryStatus", queryStatus);
        model.addAttribute("querySoftwareName", querySoftwareName);
        model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
        model.addAttribute("queryFlatformId", queryFlatformId);
        //二级分类列表和三级分类列表---回显
        if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
            categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());
            model.addAttribute("categoryLevel2List", categoryLevel2List);
        }
        if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
            categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
            model.addAttribute("categoryLevel3List", categoryLevel3List);
        }
        return "developer/appinfolist";
    }
    /**
     * 修改app信息，包括：修改app基本信息（appInfo）和修改版本信息（appVersion）
     * 分为两步实现：
     * 1 修改app基本信息（appInfo）
     *显示app基本信息
     * 2 修改版本信息（appVersion）
     */

    /**
     * 修改appInfo信息（跳转到修改appInfo页面）
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/appinfomodify",method = RequestMethod.GET)
    public String modifyAppInfo(@RequestParam(value = "id")String id,@RequestParam(value="error",required= false)
            String fileUploadError, Model model){
        AppInfo appInfo = null;
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError =" * APK信息不完整！";
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= " * 上传失败！";
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError =" * 上传文件格式不正确！";
        }else if(null != fileUploadError && fileUploadError.equals("error4")){
            fileUploadError = " * 上传文件过大！";
        }
        try {
            appInfo = appInfoService.getAppInfo(Integer.parseInt(id));
        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(appInfo);
        model.addAttribute("fileUploadError",fileUploadError);
        return "developer/appinfomodify";

    }
    /**
     * 保存修改后的appInfo
     * @param appInfo
     * @param session
     * @return
     */
    @RequestMapping(value = "/appinfomodifysave",method = RequestMethod.POST)
    public String modifySave(AppInfo appInfo, HttpSession session, HttpServletRequest request,
                             @RequestParam(value="attach",required= false) MultipartFile attach){
        String logoPicPath=null;
        String logoLocPath=null;
        String APKName=appInfo.getAPKName();
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");

            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000;
            if(attach.getSize() > filesize){//上传大小不得超过 50k
                return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error4";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
                String fileName = APKName + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path,fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                            +"&error=error2";
                }
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logoLocPath = path+File.separator+fileName;
            }else{
                return "redirect:/dev/flatform/app/appinfomodify?id="+appInfo.getId()
                        +"&error=error3";
            }
        }
        appInfo.setModifyBy(((DevUser)session.getAttribute("devUserSession")).getId());
        appInfo.setModifyDate(new Date());
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setLogoPicPath(logoPicPath);
        try {
            if(appInfoService.modify(appInfo)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "redirect:/list";//"developer/appinfolist"
    }

//删除图片
//    @RequestMapping(value = "/delfile.json",method = RequestMethod.GET)
//    public String delFile(@RequestParam String id){
//        return "developer/appinfomodify";
//    }

    public List<DataDictionary> getDataDictionaryList(String typeCode){
        List<DataDictionary> dataDictionaryList=null;
        try {
            dataDictionaryList=dataDictionaryService.getDataDictionaryList(typeCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataDictionaryList;
    }
    public List<AppCategory> getCategoryList (String pid){
        List<AppCategory> categoryLevelList = null;
        try {
            categoryLevelList = appCategoryService.getAppCategoryListByParentId(pid==null?null:Integer.parseInt(pid));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return categoryLevelList;
    }
    /**
     * 根据parentId查询出相应的分类级别列表
     * @param pid
     * @return
     */
    @RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
    @ResponseBody
    public List<AppCategory> getAppCategoryList (@RequestParam String pid){
        if(pid.equals("")) pid = null;
        return getCategoryList(pid);
    }
    /**
     * 根据typeCode查询出相应的数据字典列表
     * @param
     * @return
     */
    @RequestMapping(value="/datadictionarylist.json",method= RequestMethod.GET)
    @ResponseBody
    public List<DataDictionary> getDataDicList (@RequestParam String tcode){

        return this.getDataDictionaryList(tcode);
    }

    /**
     * 先删除app_versionl里的信息
     * 在删除app_info 里的信息
     */
    @RequestMapping(value="/delapp.json")
    @ResponseBody
    public Object delApp(@RequestParam String id){

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult", "notexist");
        }else{
            try {
                if(appInfoService.appsysdeleteAppById(Integer.parseInt(id)))
                    resultMap.put("delResult", "true");
                else
                    resultMap.put("delResult", "false");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    //上架
    @RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
    @ResponseBody
    public Object sale(@PathVariable String appid, HttpSession session){
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        Integer appIdInteger = 0;
        try{
            appIdInteger = Integer.parseInt(appid);
        }catch(Exception e){
            appIdInteger = 0;
        }
        resultMap.put("errorCode", "0");
        resultMap.put("appId", appid);
        if(appIdInteger>0){
            try {
                DevUser devUser = (DevUser)session.getAttribute("devUserSession");
                AppInfo appInfo = new AppInfo();
                appInfo.setId(appIdInteger);
                appInfo.setModifyBy(devUser.getId());
                if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
                    resultMap.put("resultMsg", "success");
                }else{
                    resultMap.put("resultMsg", "success");
                }
            } catch (Exception e) {
                resultMap.put("errorCode", "exception000001");
            }
        }else{
            //errorCode:0为正常
            resultMap.put("errorCode", "param000001");
        }

        return resultMap;
    }
    /**
     * 修改最新的appVersion信息（跳转到修改appVersion页面）
     * @param versionId
     * @param appId
     * @param model
     * @return
     */
    @RequestMapping(value="/appversionmodify",method=RequestMethod.GET)
    public String modifyAppVersion(@RequestParam("vid") String versionId,
                                   @RequestParam("aid") String appId,
                                   @RequestParam(value="error",required= false)String fileUploadError,
                                   Model model){
        AppVersion appVersion = null;
        List<AppVersion> appVersionList = null;
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError =" * APK信息不完整！";
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= " * 上传失败！";
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError =" * 上传文件格式不正确！";
        }
        try {
            appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
            appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(appVersion);
        model.addAttribute("appVersionList",appVersionList);
        model.addAttribute("fileUploadError",fileUploadError);
        return "developer/appversionmodify";
    }
    /**
     * 保存修改后的appVersion
     * @param appVersion
     * @param session
     * @return
     */
    @RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
    public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
                                       @RequestParam(value="attach",required= false) MultipartFile attach){

        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");

            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId()).getAPKName();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {

                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
                        +"&aid="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setModifyBy(((DevUser)session.getAttribute("devUserSession")).getId());//保存修改人id
        appVersion.setModifyDate(new Date());//保存修改时间
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(appVersionService.modify(appVersion)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "developer/appversionmodify";

    }
    /**
     * 查看app信息，包括app基本信息和版本信息列表（跳转到查看页面）
     * @param
     * @return
     */
    @RequestMapping(value="/appview/{id}",method=RequestMethod.GET)
    public String view(@PathVariable String id,Model model){
        AppInfo appInfo = null;
        List<AppVersion> appVersionList = null;
        try {
            appInfo = appInfoService.getAppInfo(Integer.parseInt(id));
            appVersionList = appVersionService.getAppVersionList(Integer.parseInt(id));
        }catch (Exception e) {

            e.printStackTrace();
        }
        model.addAttribute("appVersionList", appVersionList);
        model.addAttribute("appInfo",appInfo);
        return "developer/appinfoview";
    }
    /**
     * 增加app信息（跳转到新增appinfo页面）
     * @param appInfo
     * @return
     */
    @RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
    public String add(@ModelAttribute("appInfo") AppInfo appInfo){
        return "developer/appinfoadd";
    }
    /**
     * 保存新增appInfo（主表）的数据
     * @param appInfo
     * @param session
     * @return
     */
    @RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
    public String addSave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
                          @RequestParam(value="a_logoPicPath",required= false) MultipartFile attach){

        String logoPicPath =  null;
        String logoLocPath =  null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000;
            if(attach.getSize() > filesize){//上传大小不得超过 50k
                request.setAttribute("fileUploadError", "* 上传文件过大！");
                return "developer/appinfoadd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
                String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path,fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {

                    e.printStackTrace();
                    request.setAttribute("fileUploadError", "* 上传失败！");
                    return "developer/appinfoadd";
                }
                logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
                logoLocPath = path+File.separator+fileName;
            }else{
                request.setAttribute("fileUploadError", " * 上传文件格式不正确！");
                return "developer/appinfoadd";
            }
        }
        appInfo.setCreatedBy(((DevUser)session.getAttribute("devUserSession")).getId());
        appInfo.setCreationDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setDevId(((DevUser)session.getAttribute("devUserSession")).getId());
        appInfo.setStatus(1);
        try {
            if(appInfoService.add(appInfo)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "developer/appinfoadd";
    }
    /**
     * 增加appversion信息（跳转到新增app版本页面）
     * @param
     * @return
     */
    @RequestMapping(value="/appversionadd",method=RequestMethod.GET)
    public String addVersion(@RequestParam(value="id")String appId,
                             @RequestParam(value="error",required= false)String fileUploadError,
                             AppVersion appVersion,Model model){

        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError =" * APK信息不完整！";
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= " * 上传失败！";
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError =" * 上传文件格式不正确！";
        }
        appVersion.setAppId(Integer.parseInt(appId));
        List<AppVersion> appVersionList = null;
        try {
            appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
            appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId))).getSoftwareName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("appVersionList", appVersionList);
        model.addAttribute(appVersion);
        model.addAttribute("fileUploadError",fileUploadError);
        return "developer/appversionadd";
    }
    /**
     * 保存新增appversion数据（子表）-上传该版本的apk包
     * @param
     * @param appVersion
     * @param session
     * @param request
     * @param attach
     * @return
     */
    @RequestMapping(value="/addversionsave",method=RequestMethod.POST)
    public String addVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
                                 @RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");

            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = appInfoService.getAppInfo(appVersion.getAppId()).getAPKName();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {

                    e.printStackTrace();
                    return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setCreatedBy(((DevUser)session.getAttribute("devUserSession")).getId());
        appVersion.setCreationDate(new Date());
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(appVersionService.appsysadd(appVersion)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
    }
    /**
     * 判断APKName是否唯一
     * @param
     * @return
     */
    @RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
    @ResponseBody
    public Object apkNameIsExit(@RequestParam String APKName){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(APKName)){
            resultMap.put("APKName", "empty");
        }else{
            AppInfo appInfo = null;
            try {
                appInfo = appInfoService.getAppInfo( null);//,APKName
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(null != appInfo)
                resultMap.put("APKName", "exist");
            else
                resultMap.put("APKName", "noexist");
        }
        return JSONArray.toJSONString(resultMap);
    }
}
