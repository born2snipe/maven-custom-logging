package org.apache.maven.log;

public class EnvAccessor {
    public String get(String key) {
        return System.getenv(key);
    }
}
