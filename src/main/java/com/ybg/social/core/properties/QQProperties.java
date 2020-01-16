/**
 * 
 */
package com.ybg.social.core.properties;
import org.springframework.boot.autoconfigure.social.SocialProperties;

import com.ybg.enums.SocialTypeEnum;

/**
 * @author zhailiang
 *
 */
public class QQProperties extends SocialProperties {
    
    private String providerId = SocialTypeEnum.QQ.getValue();
    
    public String getProviderId(){
        return providerId;
    }
    
    public void setProviderId(String providerId){
        this.providerId = providerId;
    }
}
