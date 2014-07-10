
package com.imcore.xbionic.login.ui;
/**
 * 微博授权登陆时所需要的参数
 * @author Administrator
 *
 */
public interface Constants {
	public static final String APP_KEY = "3903710317";
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
}
