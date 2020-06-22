package com.gmail.nowak.wjw.popularmovies.network;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gmail.nowak.wjw.popularmovies.BuildConfig;
import com.gmail.nowak.wjw.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    public static final String THE_MOVIE_DATABASE_API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMD_API_MOVIE_PATH = "movie";
    public static final String TMD_API_POPULAR_PATH = "popular";
    public static final String TMD_API_TOP_RATED_PATH = "top_rated";
    private static final String THE_MOVIE_DATABASE_PRIVATE_API_KEY = BuildConfig.TMD_API_KEY;

    public static final String TMD_API_POPULAR_MOVIE_PATH = "movie/popular";
    public static final String TMD_API_TOP_RATED_MOVIE_PATH = "movie/top_rated";
    public static final String ADD_QUERY ="?";


    private static final String THE_MOVIE_DATABASE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_SMALL = "w185";
    public static final String IMAGE_SIZE_MEDIUM = "w300";
    public static final String IMAGE_SIZE_BIG = "w500";


    public static final String API_KEY_PARAM = "api_key";
    public static final String TMD_API_KEY_QUERY = API_KEY_PARAM + "=" +THE_MOVIE_DATABASE_PRIVATE_API_KEY;
    private static final String PAGE_PARAM = "page";

    public static final String POPULARITY_TAG_TITLE = "Popular";
    public static final String TOP_RATED_TAG_TITLE = "Top Rated";


    /**
     * Build Uri based on THE MOVIE DATABASE pointing to images
     *
     * @param imageUrl component of the Uri - the image path
     * @param size     size of the image
     * @return complete uri of the image
     */
    public static Uri buildTMDImageUri(String imageUrl, String size) {
        return Uri.parse(THE_MOVIE_DATABASE_IMAGE_BASE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(imageUrl)
                .appendQueryParameter(API_KEY_PARAM, THE_MOVIE_DATABASE_PRIVATE_API_KEY)
                .build();
    }

    /**
     * Fetch image using Picasso, and set it to provided view.
     *
     * @param imageUri    Uri used to fetch the image
     * @param imageView   view to set the image to
     * @param handleError set true if you want the method to handle request errors, otherwise set to false
     */
    public static void fetchImageAndSetToView(Uri imageUri, @NonNull ImageView imageView, boolean handleError) {
        if (handleError) {
            Picasso.get()
                    .load(imageUri)
                    .error(R.drawable.no_image_available_image)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(imageUri)
                    .into(imageView);
        }
    }


//    /**
//     * Build URL based on THE MOVIE DATABASE API, with a movie path.
//     *
//     * @param filter points to either popular or top rated movies
//     * @param page   set the desired page
//     * @return URL pointing to the specific path and page
//     */
//    public static URL buildTMDApiUrl(String filter, int page) {
//        String sortByValue = TMD_API_POPULAR_PATH;
//        if (filter.equals(TOP_RATED_TAG_TITLE)) {
//            sortByValue = TMD_API_TOP_RATED_PATH;
//        }
//
//        Uri mUri = Uri.parse(THE_MOVIE_DATABASE_API_BASE_URL).buildUpon()
//                .appendPath(TMD_API_MOVIE_PATH)
//                .appendPath(sortByValue)
//                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
//                .appendQueryParameter(API_KEY_PARAM, THE_MOVIE_DATABASE_PRIVATE_API_KEY)
//                .build();
//
//        URL mURL = null;
//
//        try {
//            mURL = new URL(mUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(LOG_TAG, "buildUrl URL: " + mURL.toString());
//        return mURL;
//    }
}
