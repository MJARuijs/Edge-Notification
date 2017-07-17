package mjaruijs.edge_notification.preferences;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import mjaruijs.edge_notification.services.AppList;

/**
 * Created by Marc on 7/16/2017.
 */

public interface SharedPreferencesImpl extends SharedPreferences.Editor {

    AppList getAppList(String key, @Nullable AppList defValues);

}
