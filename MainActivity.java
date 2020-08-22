package com.tcl.fftestmonitor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.idescout.sql.SqlScoutServer;
import com.tcl.fftestmonitor.MainApplication;
import com.tcl.fftestmonitor.R;
import com.tcl.fftestmonitor.component.FileChangerListener;
import com.tcl.fftestmonitor.data.databases.dao.TaskDAO;
import com.tcl.fftestmonitor.data.databases.po.Task;
import com.tcl.fftestmonitor.data.http.ApiService;
import com.tcl.fftestmonitor.data.http.DownloadFileApi;
import com.tcl.fftestmonitor.data.http.RetrofitClient;
import com.tcl.fftestmonitor.data.websocket.WebRTCSignalHandler;
import com.tcl.fftestmonitor.data.websocket.WebSocketHandler;
import com.tcl.fftestmonitor.pojo.DevInfo;
import com.tcl.fftestmonitor.pojo.Result;
import com.tcl.fftestmonitor.receiver.NetStateChangeReceiver;
import com.tcl.fftestmonitor.service.MonitorService;
import com.tcl.fftestmonitor.service.WebRTCService;
import com.tcl.fftestmonitor.util.CmdTools;
import com.tcl.fftestmonitor.util.CmdUtils;
import com.tcl.fftestmonitor.util.ConstantUtil;
import com.tcl.fftestmonitor.util.DeviceUtil;
import com.tcl.fftestmonitor.util.FileUtil;
import com.tcl.fftestmonitor.util.LogUtil;
import com.tcl.fftestmonitor.util.MonkeyUtil;
import com.tcl.fftestmonitor.util.RootSeeker;
import com.tcl.fftestmonitor.util.UrlUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tcl.fftestmonitor.util.ConstantUtil.HOST;
import static com.tcl.fftestmonitor.util.ConstantUtil.PORT;


public class MainActivity extends Activity {

    EditText editText;
    final String TAG = MainActivity.class.getName();

    // List of mandatory application permissions.／
    private static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};

    private String dir = ""; //文件存放目录
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlScoutServer.create(this, getPackageName());
        Log.d(TAG, "onCreate: ==== MainActivity =====");

//        context = getApplicationContext();
//        RetrofitClient retrofitClient = RetrofitClient.getInstance();
//        ApiService apiService = retrofitClient.createRequsest(ApiService.class);
//
//        //        Gson gson = new Gson();
//        TaskDAO taskDAO = TaskDAO.getInstance(context);
//        List<Task> res = taskDAO.queryOrderByTime();
//
//        Log.d(TAG, "onCreate sql res : " + res.size());
//        Log.d(TAG, "onCreate sql res : " + res.get(0).getDeviceIp());

//        List<Task> res = taskDAO.queryAll();
//        Log.d(TAG, "onCreate sql res : " + res.get(0).getDeviceId());


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String line = null;
        String res2 = "";
        try {
//                    CmdUtils.openAdb();
//                    String cmd1 = "monkey -p com.example.myapplication --throttle 500000 -v 10";
//            String cmd1 = "monkey -p com.tcl.vod --throttle 500 -v 30";
            Log.d(TAG, "run: " + MonkeyUtil.isUserAMonkey());

//            String cmd1 = "monkey -p com.tcl.vod --throttle 5000 -v 20 --ignore-native-crashes --ignore-crashes --ignore-timeouts 1>a.txt 2>b.txt";
//            String cmd1 = "ping www.baidu.com";
            String cmd1 = "monkey -p com.tcl.vod --throttle 500 -v 1000";

            Log.d(TAG, "run: " + cmd1 + " | " + MonkeyUtil.isUserAMonkey());
            Process p = Runtime.getRuntime().exec(cmd1);
            p.waitFor();
            Log.d(TAG, "onCreate: monkey end");
            while ((line = bufferedReader.readLine()) != null) {
                res2 = res2 + line + '\n';
//                Log.d(TAG, "onCreate: 111" + line);
            }
            BufferedReader bufferedReader = CmdUtils.exeCmd(cmd1);

            Log.d(TAG, "onCreate: monkey end" + res2);
            FileUtil.writeFile("/data/local/tmp/b.txt", res2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: ===" + res2);
//            }
//        }).start();

//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//        Map map = new HashMap();
//        map.put("sourceType", 1);
//        map.put("taskType", new ArrayList<Integer>(Arrays.asList()));
//        map.put("tvId", new ArrayList<Integer>(Arrays.asList(162)));
//        map.put("taskCodes", new ArrayList<Integer>(Arrays.asList(1)));
//        String jsonstr = "{a:1}";
//        JsonObject returnData = new JsonParser().parse(jsonstr).getAsJsonObject();
//        Log.d(TAG, "onCreate: returnData: " + returnData);
//        Log.d(TAG, "onCreate: returnData: " + returnData.get("a"));
//        String strEntity = gson.toJson(map);
//        DevInfo devInfo = DeviceUtil.readDevInfo();
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), strEntity);
//        Call<ResponseBody> call = apiService.downloadFile("/v1/api/scripts?taskcode=FF20200820000001");
//        call.enqueue(new Callback<ResponseBody>() {
//
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d(TAG, "onResponse: " + response.toString());
//                Log.d(TAG, "onResponse: " + response.message());
//                Log.d(TAG, "onResponse: " + response.code());
//                String path = "/data/local/tmp/a.zip";
//                RootSeeker.exec("chmod -R 777 /data/local/*");
//                RootSeeker.exec("chmod 777 /data/local/tmp/a.zip");
//
//                FileUtil.writeResponseBodyToDisk(path, response.body());
//                try {
//                    FileUtil.unZipFolder(path, "/data/local/tmp/zip/");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d(TAG, "fail" + t);
//
//            }
//        });

//        Call<Result<List>> call = apiService.tasksMultiQuery(body);
//
//        call.enqueue(new Callback<Result<List>>() {
//            @Override
//            public void onResponse(Call<Result<List>> call, Response<Result<List>> response) {
//                Log.d(TAG, response.toString());
//
//                Log.d(TAG, " === === === ");
//                JsonObject json = gson.toJsonTree(response.body().getData().get(0)).getAsJsonObject();
//                Task task = gson.fromJson(json, Task.class);
//
//                Log.d(TAG, String.valueOf(json.get("id")));
//                Log.d(TAG, String.valueOf(task.getDeviceId()));
//
//                Log.d(TAG, " === === === ");
//                List<Task> res = taskDAO.queryForEq("task_code", "FF20200820000001");
////                Log.d(TAG, "onCreate sql res : " + res.get(0).getDeviceIp());
//                Log.d(TAG, " update sql ");
//
//                if (res.get(0).getTaskcode().equals("FF20200820000001")) {
//                    Log.d(TAG, " update sql ");
//                    taskDAO.update(task);
//                } else {
//                    Log.d(TAG, " insert sql ");
//                    taskDAO.insert(task);
//
//                }
//                Log.d(TAG, " === === === onResponse");
//
//            }
//
//            private Context getApplication() {
//                return getApplication();
//            }
//
//            @Override
//            public void onFailure(Call<Result<List>> call, Throwable t) {
//                Log.d(TAG, "fail");
//                Log.d(TAG, "fail" + t);
//            }
//        });


//        TaskDAO taskDAO = TaskDAO.getInstance(getApplication());
//        Log.d(TAG, "onCreate: " + taskDAO.queryAll());
//        initService(TaskService.class);

//        startService(new Intent(this, TaskService.class));
        Log.d(TAG, "onCreate: ==== MainActivity end =====");

//        MonkeyTask.runTask("monkey -p com.tcl.vod --throttle 5000 -v 20");
//        MonkeyUtil.getMonkeyStatus();


        //        Scripts s = YamlUtil.getContent(getApplicationContext(), "011_屏保检查.yaml");
//        Log.d(TAG, "onCreate: " + s);
//        Log.d(TAG, "onCreate: " + s.getScript_steps());
//        Log.d(TAG, "onCreate: " + s.getScript_steps().size());
//        for (int i = 0; i < s.getScript_steps().size(); i++) {
//            if (i == 0) {
//                Log.d(TAG, "onCreate1: " + s.getScript_steps().get(i));
//                Log.d(TAG, "onCreate2: " + s.getScript_steps().get(i).getSteps());
//                Log.d(TAG, "onCreate2: " + s.getScript_steps().get(i).getSteps().get(0).getData());
//                Log.d(TAG, "onCreate3: " + s.getScript_steps().get(i).getSteps().size());
//            }
//        }

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.monitor_activity_main);
//        initialize();
    }


    private void initialize() {

        RootSeeker.exec("pkill ffmonitor*");

        editText = findViewById(R.id.current_time);
        editText.setVisibility(View.GONE);

        //初始化监听目录
        String savePath = this.getCacheDir().getAbsolutePath();
        if (new File(savePath).exists()) {
            ConstantUtil.MONITOR_DIR = this.getCacheDir().getAbsolutePath();
        }

        if (!EasyPermissions.hasPermissions(this, MANDATORY_PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, "Need permissions for camera & microphone", 0, MANDATORY_PERMISSIONS);
        }


        //初始化服务器地址
        try {
//          File file=new File("/data/local/tmp/autotest.json");
            String host = "";
            boolean configFileExist = checkFileExist("/data/local/tmp/autotest.json");
            if (configFileExist) {
                host = UrlUtil.getHOST(this.getApplicationContext());
            }
            if (host == null || host.equals("")) {
                //            host=HOST;
            } else {
                HOST = host;
                ConstantUtil.SERVER_ADDRESS = UrlUtil.getServerAddress(this.getApplicationContext());
            }

            ///storage/sdcard0/0/Android/data/com.tcl.fftestmonitor/files/Download
//            File externalFilePath=getExternalFilesDir(DIRECTORY_DOWNLOADS);
//            if (externalFilePath==null||externalFilePath.getAbsolutePath().equals("")||!externalFilePath.exists()){
//                dir="/data/local/tmp";
//            }else{
//                dir=externalFilePath.getAbsolutePath();
//            }
            dir = "/data";
            String filename = "ffmonitor7";
            String path = dir + "/" + filename;
            boolean monitorExist = checkFileExist(path);
            if (!monitorExist) {
                RootSeeker.exec("rm  /data/ffmonitor*");
                RootSeeker.exec("pkill ffmonitor*");
                DownloadFileApi downloadFileApi = new DownloadFileApi(null, new FileChangerListener() {
                    @Override
                    public void onFileChanged(String path) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(30 * 1000);
                                    RootSeeker.chmod(path);
                                    Thread.sleep(3 * 1000);
                                    RootSeeker.exec(String.format("nohup %s >/dev/null 2>&1 &", path));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                downloadFileApi.download(dir, filename);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

//                            DatabaseHelper.getHelper(MainApplication.getContextObject());
//
//                            try {
//
////                                TaskDAO taskDAO= (TaskDAO) DatabaseHelper.getHelper(MainApplication.getContextObject()).getDao(Task.class);
//                                Task task=new Task();
//                                task.setApkUrl("");
//                                task.setStartTime(new Date());
////                                taskDAO.insert(task);
//                                TaskDAO.getInstance(MainApplication.getContextObject()).insert(task);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

                            RootSeeker.exec("pkill ffmonitor*");
                            Thread.sleep(50 * 1000);
//                            String result = CmdTools.execAdbCmd("ps -ef | grep ffmonitor",5000);
//                            if(result==null|| result.equals("")){
//                                result = CmdTools.execAdbCmd("ps | grep ffmonitor",5000);
//                            }
//                            if (result!=null&& !result.equals("")&&result.contains(filename)){
//                                    return;
//                            }
                            RootSeeker.exec(String.format("nohup %s >/dev/null 2>&1 &", path));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //启动监听程序
        appStartProcess();
    }

    private void appStartProcess() {
        if (WebSocketHandler.isConnected && WebRTCSignalHandler.isConnected) {
            CmdTools.execCmd("input keyevent 3");
            return;
        }


        initService(MonitorService.class);

        initService(WebRTCService.class);

    }

    private void initService(Class _class) {
        Intent intent = new Intent(this, _class);
        Bundle bundle = new Bundle();
        bundle.putString("host", HOST);
//        bundle.putString("host","fftest.imwork.net");
        bundle.putInt("port", PORT);
        intent.putExtras(bundle);
        Log.d(TAG, "initService: dsdsd" + Build.VERSION.SDK_INT + " === " + Build.VERSION_CODES.O);
        Log.d(TAG, "initService: dsdsd" + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "initService: startForegroundService" + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O));

            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public boolean checkFileExist(String path) {
        File file = new File(path);
        if (!file.exists())
            return false;
        return true;
    }

    private boolean checkMonitor() {
        try {
            String result = CmdTools.execAdbCmd("ps -ef | grep ffmonitor", 5000);
            if (result == null || result.equals("")) {
                result = CmdTools.execAdbCmd("ps | grep ffmonitor", 5000);
            }
            if (result != null && !result.equals("")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void killMonitor() {
        try {
            String result = CmdTools.execAdbCmd("ps -ef | grep ffmonitor", 3000);
            if (result == null || result.equals("")) {
                result = CmdTools.execAdbCmd("ps | grep ffmonitor", 3000);
            }
            String contentArr[] = result.split("\n");
            for (String item : contentArr) {
                if (item != null && !item.equals("")) {
                    //root      6897  1     800340 4660  SyS_epoll_ 0000000000 S ./ffmonitor3
                    String pid = item.trim().split(" +")[1];
                    RootSeeker.exec("kill -9 " + pid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void register() {
        NetStateChangeReceiver receiver = new NetStateChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        MainApplication.getContextObject().registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestory");
    }
}
