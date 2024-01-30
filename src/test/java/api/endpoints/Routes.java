package api.endpoints;

public class Routes {
    public static String base_url = "https://favqs.com/api";
    // user module
    public static String create_user = base_url + "/users";
    //Quotes module

    public static String get_url = base_url + "/quotes";
    public static String update_url = base_url + "/quotes/{quote_id}/fav";
    public static String update_unFav_url = base_url + "/quotes/{quote_id}/unfav";

}
