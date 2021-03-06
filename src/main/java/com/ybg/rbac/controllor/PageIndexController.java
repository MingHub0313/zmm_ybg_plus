package com.ybg.rbac.controllor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ybg.base.util.SystemConstant;
import com.ybg.rbac.RbacConstant;
import com.ybg.rbac.resources.service.ResourcesService;
import com.ybg.rbac.user.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Deament
 * 
 * @date 2016/9/31
 ***/
@Api(tags = "平台角色首页加载API")
@Controller
public class PageIndexController {
    
    @Autowired
    ResourcesService resourcesService;
    
    // 旧版 屏蔽
    // @ApiOperation(value = "登录成功后的页面 ", notes = "", produces = MediaType.TEXT_HTML_VALUE)
    // @RequestMapping(value = "/common/login_do/index.do", method = { RequestMethod.GET, RequestMethod.POST })
    // public String index(Model model) throws Exception {
    // User user = (User) Common.findUserSession();
    // if (user == null) {
    // return "redirect:/common/login_do/tologin.do";
    // }
    // // 这里不直接传入userid,以后用户表变大 查询将变慢
    // List<SysResources> mps = resourcesService.getRolesByUserId(user.getRoleid());
    // // 转化成树
    // List<TreeObject> list = new ArrayList<TreeObject>();
    // for (SysResources map : mps) {
    // TreeObject ts = new TreeObject();
    // ts.setDescription(map.getDescription());
    // ts.setIcon(map.getIcon());
    // ts.setId(map.getId());
    // ts.setIshide(map.getIshide());
    // ts.setLevel(map.getLevel());
    // ts.setName(map.getName());
    // ts.setParentId(map.getParentid());
    // ts.setResKey(map.getReskey());
    // ts.setResUrl(map.getResurl());
    // ts.setType(map.getType());
    // ts.setColorclass(map.getColorclass());
    // list.add(ts);
    // }
    // TreeUtil treeUtil = new TreeUtil();
    // List<TreeObject> ns = treeUtil.getChildTreeObjects(list, "0");
    // model.addAttribute("blist", ns);
    // // 登陆的信息回传页面
    // model.addAttribute("userFormMap", user);
    // return "/index";
    // }
    // 新版index 拆分结构
    @ApiOperation(value = "登录成功后的页面 ",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/index.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String index(@AuthenticationPrincipal UserDTO user,Model model) throws Exception{
        if (user == null) {
            return "redirect:/common/login_do/tologin.do";
        }
        // 取消加载 菜单
        // 登陆的信息回传页面
        model.addAttribute("systemname", SystemConstant.getSystemName());
        model.addAttribute("systemauth", SystemConstant.getSystemAuth());
        model.addAttribute("icp", SystemConstant.getICP());
        model.addAttribute("systemdomain", SystemConstant.getSystemdomain());
        model.addAttribute("userFormMap", user);
        return "/index/admin/index";
    }
    
    /** 加载菜单 **/
    @ApiOperation(value = "登录成功后的页面-菜单 ",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/menu.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String menu(@AuthenticationPrincipal UserDTO user,ModelMap map){
        if (user == null) {
            return "";
        }
        map.put("userFormMap", user);
        if (RbacConstant.isAdmin(user)) {
            return "/index/admin/menu";
        }
        if (RbacConstant.isOther(user)) {
            return "/index/other/menu";
        }
        return "";
    }
    
    /** 加载子系統菜单 **/
    @ApiOperation(value = "非超管加载OA子系統菜单-菜单 ",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/menu_oa.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String menuOa(@AuthenticationPrincipal UserDTO user,ModelMap map){
        if (user == null) {
            return "";
        }
        map.put("userFormMap", user);
        return "/index/other/menu_oa";
    }
    
    /** 加载子系統菜单 **/
    @ApiOperation(value = "非超管加载教育子系統菜单-菜单 ",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/menu_edu.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String menuEdu(@AuthenticationPrincipal UserDTO user,ModelMap map){
        if (user == null) {
            return "";
        }
        map.put("userFormMap", user);
        return "/index/other/menu_edu";
    }
    
    /** 加载头部 **/
    @ApiOperation(value = "登录成功后的页面 -头部",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/head.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String head(@AuthenticationPrincipal UserDTO user,ModelMap map){
        if (user == null) {
            return "";
        }
        map.put("userFormMap", user);
        return "/index/admin/head";
    }
    
    /** 加载欢迎页面 **/
    @ApiOperation(value = "登录成功后的页面-欢迎页面 ",notes = "",produces = MediaType.TEXT_HTML_VALUE)
    @RequestMapping(value = "/common/login_do/welcome.do",method = { RequestMethod.GET, RequestMethod.POST })
    public String welcome(@AuthenticationPrincipal UserDTO user,ModelMap map){
        if (user == null) {
            return "";
        }
        map.put("userFormMap", user);
        if (RbacConstant.isAdmin(user)) {
            return "/index/admin/welcome";
        }
        if (RbacConstant.isOther(user)) {
            // 让用户选择子系统
            return "/index/other/welcome";
        }
        return "";
    }
}
