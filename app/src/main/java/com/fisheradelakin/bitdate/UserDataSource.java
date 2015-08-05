package com.fisheradelakin.bitdate;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fisher on 8/3/15.
 */
public class UserDataSource {

    private static User sCurrentUser;

    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_PICTURE_URL = "pictureURL";

    public static User getCurrentUser() {
        if(sCurrentUser == null && ParseUser.getCurrentUser() != null) {
            sCurrentUser = parseUsertoUser(ParseUser.getCurrentUser());
        }

        return sCurrentUser;
    }

    public static void getUnseenUsers(final UserDataCallbacks callbacks) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", getCurrentUser().getId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null) {
                    List<User> users = new ArrayList<>();
                    for(ParseUser parseUser : parseUsers) {
                        User user = parseUsertoUser(parseUser);
                        users.add(user);
                    }
                    if(callbacks != null) {
                        callbacks.onUsersFetched(users);
                    }
                }
            }
        });
    }

    private static User parseUsertoUser(ParseUser parseUser) {
        User user = new User();
        user.setFirstName(parseUser.getString(COLUMN_FIRST_NAME));
        user.setPictureURL(parseUser.getString(COLUMN_PICTURE_URL));
        user.setId(parseUser.getObjectId());
        return user;
    }

    public interface UserDataCallbacks {
        void onUsersFetched(List<User> users);
    }
}
