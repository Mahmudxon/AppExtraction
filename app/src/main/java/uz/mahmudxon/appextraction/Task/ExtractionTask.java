 package uz.mahmudxon.appextraction.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.storage.StorageManager;
import uz.mahmudxon.appextraction.Model.AppModel;

import java.util.ArrayList;

public class ExtractionTask extends AsyncTask<Void, Integer, String> {
    ArrayList<AppModel> data;
    Context context;
    ProgressDialog dialog;
    int count;
    OnTaskFinishListener onTaskFinishListener;

    public void setOnTaskFinishListener(OnTaskFinishListener onTaskFinishListener) {
        this.onTaskFinishListener = onTaskFinishListener;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Extracting...");
        dialog.setMax(count);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
    }

    public ExtractionTask(ArrayList<AppModel> data, Context context, Integer count) {
        this.data = data;
        this.context = context;
        this.count = count;
    }

    @Override
    protected String doInBackground(Void... voids) {
        ArrayList<AppModel> temp = new ArrayList<>();

        for (AppModel app : data)
        {
            if(app.isChecked())
            {
                temp.add(app);
                app.setChecked(false);
            }
        }



        int n = 0;
        for (AppModel app : temp)
        {
             Extract.Companion.ExtractApp(app);

            publishProgress(++n);
        }



        return "Extraction complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        if(onTaskFinishListener != null)
        {
            onTaskFinishListener.onFinish();
        }
    }


    public interface OnTaskFinishListener
    {
        void onFinish();
    }

}
