package com.association.user.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.association.config.iface.ConfigIface;
import com.association.user.mapper.RoleMapper;
import com.association.user.Iface.RoleIface;
import com.association.user.model.Auth;
import com.association.user.model.AuthDTO;
import com.association.user.model.RoleDO;
import component.BasicService;
import component.Proto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RoleServiceImpl extends BasicService implements RoleIface {
    @Autowired
    ConfigIface configIface;

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Proto<List<String>> getUrlsByRole(RoleDO role) {
        RoleDO roleDO = roleMapper.getRoleByGuid(role.getGuid());
        List<String> auth = JSONArray.parseArray(roleDO.getAuthJson(), String.class);
        Proto<com.association.config.model.AuthDTO> configAuth = configIface.auth();
        if (configAuth != null) {
           // List<String> allAuthUrls = allAuthUrls(configAuth.getData());
            Proto<List<Auth>> userAuth = getAuthByGuid(role.getGuid());
            if(userAuth != null){
                List<String> refAuthUrls = refAuthUrls(userAuth.getData());
                return getResult(refAuthUrls);
            }
        }
        return getResult(Collections.emptyList());
    }

    //todo 后台管理页面 不重要
    @Override
    public Proto<AuthDTO> getRoleTree() {
        Proto<com.association.config.model.AuthDTO> proto = configIface.auth();
        Proto<AuthDTO> authDTOProto = JSONObject.parseObject(JSONObject.toJSONString(proto), Proto.class);
        return authDTOProto;
    }

    //todo
    @Override
    public Proto<Boolean> updateRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<Boolean> createNewRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<Boolean> deleteRole(RoleDO role) {
        return null;
    }

    @Override
    public Proto<List<Auth>> getAuthByGuid(String guid) {
        RoleDO roleDO = roleMapper.getRoleByGuid(guid);
        String json = roleDO.getAuthJson();
        List<String> auths = JSONArray.parseArray(json, String.class);
        Proto<com.association.config.model.AuthDTO> authDTOProto = configIface.auth();
        List<Auth> result = Collections.EMPTY_LIST;
        if (authDTOProto != null) {
            result = authDTOProto.getData().getAuth().stream().filter(auth -> auths.contains(auth.getId())).collect(Collectors.toList());
        }
        return getResult(result);
    }

    public List<String> refAuthUrls(List<Auth> auths) {
        List<String> urls = new ArrayList<>();
        dfs(auths, urls);
        return urls;
    }

    public List<String> allAuthUrls(com.association.config.model.AuthDTO authDTO) {
        List<Auth> auths = authDTO.getAuth();
        List<String> urls = new ArrayList<>();
        dfs(auths, urls);
        return urls;
    }

    public List<String> dfs(Auth auth, List<String> result) {
        if (auth == null) {
            return result;
        }
        //todo getUrls 需要去除禁用的
        result.addAll(auth.getUrls());
        dfs(auth, result);
        return result;
    }

    public List<String> dfs(List<Auth> auths, List<String> result) {
        for (Auth auth : auths) {
            dfs(auth, result);
        }
        return result;
    }
}
