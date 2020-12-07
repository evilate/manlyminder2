package eu.allan.apps.manlyminder2;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
public class _TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        //Log.i(TAG, "TestJobService");
        //Intent service = new Intent(getApplicationContext(), LocalWordService.class);
        //getApplicationContext().startService(service);
//        NotificationCreator nc = new NotificationCreator(this);
//        nc.sendNotification("");
        _Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}