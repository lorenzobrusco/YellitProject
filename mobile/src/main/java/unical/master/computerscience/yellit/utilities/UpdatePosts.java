package unical.master.computerscience.yellit.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.connection.PostGestureService;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Post;

/**
 * Used to update posts
 */

public class UpdatePosts {

    /**
     * Get all posts
     *
     * @param context
     */
    public static void getAllPost(final Context context) {

        final List<Post> postsToShow = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostGestureService postService = retrofit.create(PostGestureService.class);
        Call<Post[]> call = postService.getAllPosts("getAll");
        call.enqueue(new Callback<Post[]>() {
            @Override
            public void onResponse(Call<Post[]> call, Response<Post[]> response) {

                Post[] posts = response.body();

                if (posts != null)
                    for (int i = posts.length - 1; i >= 0; i--) {
                        postsToShow.add(posts[i]);
                    }

                InfoManager.getInstance().setmPostList(postsToShow);
                InfoManager.getInstance().setmPostFilteredList(postsToShow);
            }

            @Override
            public void onFailure(Call<Post[]> call, Throwable t) {
                Log.e("On failure", "Post looking for");
            }
        });
    }

    /**
     * Load at the first time all posts
     *
     * @param context
     */
    public static void loadAllPost(final Context context) {

        final List<Post> postsToShow = InfoManager.getInstance().getmPostFilteredList();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostGestureService postService = retrofit.create(PostGestureService.class);
        Call<Post[]> call = postService.getAllPosts("getAll");
        call.enqueue(new Callback<Post[]>() {
            @Override
            public void onResponse(Call<Post[]> call, Response<Post[]> response) {

                Post[] posts = response.body();

                if (posts != null)
                    for (int i = posts.length - 1; i >= 0; i--) {
                        if(!containsIdPost(posts[i].getIdPost())){
                            postsToShow.add(posts[i]);
                            Log.e("UpdtePosts", posts[i].getIdPost()+"");
                        }
                        else {
                            break;
                        }
                    }

                InfoManager.getInstance().setmPostList(postsToShow);
                InfoManager.getInstance().setmPostFilteredList(postsToShow);
                final PostAdapter mPostAdapter = new PostAdapter(context, postsToShow);
                InfoManager.getInstance().setmPostAdapter(mPostAdapter);
            }

            @Override
            public void onFailure(Call<Post[]> call, Throwable t) {
                Toast.makeText(context, "Error from loading new posts", Toast.LENGTH_SHORT).show();
                Log.e("On failure", "Post looking for");
            }
        });
    }


    private static boolean containsIdPost(int newIdPost){
        for(Post post : InfoManager.getInstance().getmPostFilteredList()){
            if(post.getIdPost() == newIdPost)
                return true;
        }
        return false;
    }
}
