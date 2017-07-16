package tonopurwanto.jabberapp.di;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.inject.Singleton;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;

@Module
public class XmppModule
{
    @Provides @Singleton
    public X509TrustManager provideTrustManager()
    {
        return new X509TrustManager()
        {
            @Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {

            }

            @Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {

            }

            @Override public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }
        };
    }
}
