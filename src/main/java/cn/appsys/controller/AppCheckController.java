package cn.appsys.controller;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.AppCategoryService;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.AppVersionService;
import cn.appsys.service.DataDictionaryService;
import cn.appsys.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/manager/backend/app")
public class AppCheckController {

    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private AppCategoryService appCategoryService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private AppVersionService appVersionService;
    @RequestMapping(value="/list")
    public String getAppInfoList(Model model, HttpSession session,
                                 @RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
                                 @RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
                                 @RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
                                 @RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
                                 @RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
                                 @RequestParam(value="pageIndex",required=false) String pageIndex){

        List<AppInfo> appInfoList = null;
        List<DataDictionary> flatFormList = null;
        List<AppCategory> categoryLevel1List = null;//列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
        List<AppCategory> categoryLevel2List = null;
        List<AppCategory> categoryLevel3List = null;
        //页面容量
        int pageSize = 5;
        //当前页码
        Integer currentPageNo = 1;

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch (NumberFormatException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
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
            queryFlatformId = Integer.parseInt(_queryFlatformId);//
        }
        Integer queryStatus=null;
        Integer devId=null;
        //总数量（表）
        int totalCount = 0;
        try {
            totalCount = appInfoService.getAppInfoCount(querySoftwareName,null,queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,null);
        } catch (Exception e) {

            e.printStackTrace();
        }

        //总页数
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        System.out.println("currentPageNo"+currentPageNo);
        try {
            appInfoList = appInfoService.getAppInfoList(querySoftwareName,null,queryCategoryLevel1,
                    queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,null, currentPageNo, pageSize);
            flatFormList = this.getDataDictionaryList("APP_FLATFORM");
            categoryLevel1List = appCategoryService.getAppCategoryListByParentId(null);
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.out.println(appInfoList);
        model.addAttribute("appInfoList", appInfoList);
        model.addAttribute("flatFormList", flatFormList);
        model.addAttribute("categoryLevel1List", categoryLevel1List);
        model.addAttribute("pages", pages);
        model.addAttribute("querySoftwareName", querySoftwareName);
        model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
        model.addAttribute("queryFlatformId", queryFlatformId);

        //二级分类列表和三级分类列表---回显
        if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
            categoryLevel2List = getCategoryList(queryCategoryLevel1.toString());//调用下面的
            model.addAttribute("categoryLevel2List", categoryLevel2List);
        }
        if(queryCategoryLevel3 != null && !queryCategoryLevel3.equals("")){
            categoryLevel3List = getCategoryList(queryCategoryLevel2.toString());
            model.addAttribute("categoryLevel3List", categoryLevel3List);
        }
        return "backend/applist";
    }
    public List<DataDictionary> getDataDictionaryList(String typeCode){
        List<DataDictionary> dataDictionaryList = null;
        try {
            dataDictionaryList = dataDictionaryService.getDataDictionaryList(typeCode);
        } catch (Exception e) {

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
    @RequestMapping(value="/categorylevellist.json",method= RequestMethod.GET)
    @ResponseBody
    public List<AppCategory> getAppCategoryList (@RequestParam String pid){
//        logger.debug("getAppCategoryList pid ============ " + pid);
        if(pid.equals("")) pid = null;
        return getCategoryList(pid);
    }
    //    @RequestMapping("/list")
//    public String getAppInfoList(Model model){
//        List<AppInfo> appInfos=new ArrayList<>();
//        appInfos=appInfoService.getAppInfo();
//        model.addAttribute("appInfoList",appInfos);
//        return "backend/applist";
//    }
    /**
     * 跳转到APP信息审核页面
     * @param appId
     * @param versionId
     * @param model
     * @return
     */
    @RequestMapping(value="/check",method=RequestMethod.GET)
    public String check(@RequestParam(value="aid",required=false) String appId,
                        @RequestParam(value="vid",required=false) String versionId,
                        Model model){
        AppInfo appInfo = null;
        AppVersion appVersion = null;
        try {
            appInfo = appInfoService.getAppInfo(Integer.parseInt(appId));
            appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
        }catch (Exception e) {

            e.printStackTrace();
        }
        model.addAttribute("appVersion",appVersion);
        model.addAttribute("appInfo",appInfo);
        return "backend/appcheck";
    }
    @RequestMapping(value="/checksave",method=RequestMethod.POST)
    public String checkSave(AppInfo appInfo){
//        logger.debug("appInfo =========== > " + appInfo.getStatus());
        try {
            if(appInfoService.updateSatus(appInfo.getStatus(),appInfo.getId())){
                return "redirect:/manager/backend/app/list";
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return "backend/appcheck";
    }
}
