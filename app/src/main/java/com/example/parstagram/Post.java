package com.example.parstagram;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class represents a single post object.
 */
@ParseClassName("Post") // entity (class) in parse
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    /**
     * Used to get the description of the post.
     *
     * @return the description of the post as a string
     */
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    /**
     * Used to set the description of the post to the given string.
     *
     * @param description   the description of the post to set
     */
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    /**
     * Used to get the user that wrote the post.
     *
     * @return the user that wrote the post as a ParseUser
     */
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    /**
     * Used to set the author of the post.
     *
     * @param user  the user of the post to set as a ParseUser
     */
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    /**
     * Used to get the image associated with the post.
     *
     * @return  the image associated with the post as a ParseFile
     */
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    /**
     * Used to set the image associated with the post.
     *
     * @param parseFile the image associated with the post to set as a ParseFile
     */
    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }
}
