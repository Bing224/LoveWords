package tedking.LoveWords;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private View guide1, guide2, guide3,guide4;
    private Button enter;
    private List<View> viewList;
    public void findview(){
        viewPager = (ViewPager) findViewById(R.id.guidepager);
        LayoutInflater inflater = getLayoutInflater();
        guide1 = inflater.inflate(R.layout.guide1,null);
        guide2 = inflater.inflate(R.layout.guide2,null);
        guide3 = inflater.inflate(R.layout.guide3,null);
        guide4 = inflater.inflate(R.layout.guide4,null);
        viewList = new ArrayList<View>();
        viewList.add(guide1);
        viewList.add(guide2);
        viewList.add(guide3);
        viewList.add(guide4);
        final PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object){
                container.removeView(viewList.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(adapter);
        enter = (Button) guide4.findViewById(R.id.enter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findview();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                GuideActivity.this.finish();
            }
        });
    }
}
