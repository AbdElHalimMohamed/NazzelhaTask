package com.abdelhalim.nazzelhatask;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class Constants {
    public final static String REPOSITORIES_URL = "https://api.github.com/users/square/repos?";
    public final static String AUTH_URL = "https://github.com/login/oauth/authorize?";
    public final static String TOKEN_URL = "https://github.com/login/oauth/access_token?";
    public final static String USER_URL = "https://api.github.com/user?";
    public final static int PER_PAGE_ITEMS = 50;
    public final static String PER_PAGE_URL_PARAMETER_KEY = "per_page";
    public final static String CLIENT_ID_KEY = "client_id";
    public final static String CLIENT_SECRET_KEY = "client_secret";
    public final static String REDIRECT_URL_KEY = "redirect_uri";
    public final static String CODE_KEY = "code";
    public final static String ACCESS_TOKEN_KEY = "access_token";
    public final static String LINK_HEADER_KEY = "Link";
    public final static String REPO_NAME_KEY = "name";
    public final static String REPO_DESC_KEY = "description";
    public final static String REPO_OWNER_KEY = "owner";
    public final static String REPO_OWNER_NAME_KEY = "login";
    public final static String REPO_FORK_KEY = "fork";
    public final static String REPO_HTML_URL = "html_url";
    public final static String USER_NAME_KEY = "login";
    public final static String REPO_USER_HTML_URL = "html_url";
    public final static String LINK_NEXT_KEY = "\"next\"";
    public final static int REFRESH_THRESHOLD = 10;
    public final static String DIALOG_EXTRA_TITLE = "title";
    public final static String DIALOG_EXTRA_MESSAGE = "message";
}
