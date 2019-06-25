package com.ahao.util.spring.shiro;

import com.ahao.util.commons.lang.ReflectHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShiroHelper {
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static PrincipalCollection getPrincipals() {
        Subject subject = getSubject();
        return subject == null ? null : subject.getPrincipals();
    }

    public static List<Realm> getRealms() {
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        if(securityManager instanceof RealmSecurityManager) {
            RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            return Collections.unmodifiableList(new ArrayList<>(realmSecurityManager.getRealms()));
        }
        return Collections.emptyList();
    }

    /**
     * 清除当前登录用户的权限缓存
     * @return 是否成功
     */
    public static boolean clearCache() {
        PrincipalCollection principals = getPrincipals();
        if(principals == null) {
            return false;
        }

        List<CachingRealm> realms = getRealms().stream()
            .filter(c -> CachingRealm.class.isAssignableFrom(c.getClass()))
            .map(c -> (CachingRealm) c)
            .collect(Collectors.toList());

        for (CachingRealm realm : realms) {
            ReflectHelper.executeMethod(realm, "clearCache", principals);
        }
        return true;
    }
}
