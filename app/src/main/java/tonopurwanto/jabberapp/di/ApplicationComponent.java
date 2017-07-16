package tonopurwanto.jabberapp.di;

import javax.inject.Singleton;

import dagger.Component;
import tonopurwanto.jabberapp.MainActivity;
import tonopurwanto.jabberapp.XmppService;

@Singleton
@Component(modules = {ApplicationModule.class, XmppModule.class})
public interface ApplicationComponent
{
    void inject(MainActivity target);
    void inject(XmppService target);
}
