package common;

public class SettingsMessage extends Message {
    int maxDigit;
    boolean allowDublicates;
    
    public SettingsMessage(String message){
        super(message);
    }

    public SettingsMessage(int max, boolean allowDublicates){
        super(max + "");
        this.maxDigit=max;
        this.allowDublicates=allowDublicates;
    }

    public int getMaxdigit()
    {
        return this.maxDigit;
    }

    public boolean getAllowdublicates()
    {
        return this.allowDublicates;
    }
}
