package tonopurwanto.jabberapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tonopurwanto.jabberapp.di.ApplicationComponent;
import tonopurwanto.jabberapp.di.ApplicationModule;
import tonopurwanto.jabberapp.di.DaggerApplicationComponent;

public class JabberApp extends Application
{
    private ApplicationComponent component;

    @Override public void onCreate()
    {
        super.onCreate();

        // Dagger configuration
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();

        // Realm configuration
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("jabberapp.realm")
                .build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public ApplicationComponent getComponent()
    {
        return component;
    }
}
