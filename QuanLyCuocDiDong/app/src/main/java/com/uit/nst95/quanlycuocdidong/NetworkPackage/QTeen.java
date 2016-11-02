package com.uit.nst95.quanlycuocdidong.NetworkPackage;

/**
 * Created by JT on 18/10/2016.
 */

public class QTeen extends PackageFee
{
    private int _exceptionCallFee;

    public QTeen()
    {
        this._callTime = "";
        this._internalCallFee = 1280;
        this._outerCallFee = 1480;
        this._internalMessageFee = 200;
        this._outerMessageFee = 250;
        this._exceptionCallFee = 640;
    }


    public int CalculateCallFee()
    {
        if(this._numberHeader.isInternalNetwork(this._myNetwork, this._outGoingPhoneNumber))
        {
            if(this.isSpecialTime())
            {
                if(this._callDuration <= this._callBlock)
                    this._callFee = this._exceptionCallFee/10;
                else
                {
                    int remainDuration  = this._callDuration - this._callBlock;
                    this._callFee = this._exceptionCallFee/10 +  remainDuration*Math.round(((float)this._exceptionCallFee/60));

                }
            }
            return this._callFee;
        }
        return super.CalculateCallFee();

    }
}