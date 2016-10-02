package com.uit.nst95.quanlycuocdidong.Manager;

import java.io.Serializable;

/**
 * Created by QNghia on 02/10/2016.
 */

public class PackageNetwork implements Serializable {
    private String packageName;
    private int idImage;
    private String providerName;
    public PackageNetwork(String providername, String packagenam, int idimage)
    {
        providerName = providername;
        packageName = packagenam;
        idImage = idimage;
    }
    public String getProviderName(){
        return providerName;
    }
    public String getPackageName()
    {
        return packageName;
    }
    public int getIdImage()
    {
        return idImage;
    }

}
