package com.example.myweathernow.exception;

/**
 * Created by ele on 05/01/15.
 */
public class LocationNotStoredException extends Exception
{
    //Parameterless Constructor
    public LocationNotStoredException() {}

    //Constructor that accepts a message
    public LocationNotStoredException(String message)
    {
        super(message);
    }
}
