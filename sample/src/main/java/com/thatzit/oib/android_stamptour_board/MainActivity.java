package com.thatzit.oib.android_stamptour_board;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thatzit.oib.stamptourboard.StampTourBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> townNames = new ArrayList<String>();

    private static final int BOARD_REQUEST_CODE = 3333;

    private final String TEST_REGION = "보령시";
    private final String TEST_APPLICATIONID = "보령스탬프투어";
    private final String TEST_NICK = "";
    private final String TEST_ACCESSTOKEN = "";
    private final String HINT_SELECTEDTOWNNAME="장소";
    private final String TEST_SHARE_PACKAGENAME = "com.thatzit.oib";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        townNames.add("장소1");
        townNames.add("장소2");
        townNames.add("장소3");
        townNames.add("장소4");

        Button btn = (Button) findViewById(R.id.btn_board);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StampTourBoard stampTourBoard = StampTourBoard.create(MainActivity.this);
                stampTourBoard.applicationRegion(TEST_REGION)
                        .applicationID(TEST_APPLICATIONID)
                        .userNick(TEST_NICK)
                        .userAccesstoken(TEST_ACCESSTOKEN)
                        .selectTownNames(townNames)
                        .hintSelectTownName(HINT_SELECTEDTOWNNAME)
                        .sharePacakgeName(TEST_SHARE_PACKAGENAME)
                        .start(BOARD_REQUEST_CODE);
            }
        });
    }
}
