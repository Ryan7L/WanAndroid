package per.goweii.basic.core.mvp;

import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public abstract class CacheActivity extends AppCompatActivity {

    private SparseArray<View> mViewCaches = null;

    @Override
    public <T extends View> T findViewById(int id) {
        if (mViewCaches == null) {
            mViewCaches = new SparseArray<>();
        }
        View view = mViewCaches.get(id);
        if (view == null) {
            view = getWindow().getDecorView().findViewById(android.R.id.content).findViewById(id);
            mViewCaches.put(id, view);
        }
        return (T) view;
    }

}
