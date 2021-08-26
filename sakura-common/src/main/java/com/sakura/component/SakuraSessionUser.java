package com.sakura.component;

import com.sakura.web.session.DefaultSessionUser;
import org.springframework.stereotype.Component;

@Component
public class SakuraSessionUser extends DefaultSessionUser {


    public void setPermission(Long userId, String value){
        this.set(userId + "permission", value);
    }

    public String getPermission(String key){
        return this.get(key);
    }


}
