package tonopurwanto.jabberapp.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tonopurwanto.jabberapp.BuildConfig;

@Module
public class ApplicationModule
{
    private Application application;

    public ApplicationModule(Application application)
    {
        this.application = application;
    }

    @Provides @Singleton
    public Context provideContext()
    {
        return this.application;
    }

    @Provides @Singleton
    public SharedPreferences provideSharedPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton
    public EventBus provideEventBus()
    {
        return EventBus.builder()
                .logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(BuildConfig.DEBUG)
                .build();
    }
}
