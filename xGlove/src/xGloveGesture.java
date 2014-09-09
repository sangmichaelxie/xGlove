class xGloveGesture 
{
	//Used in debug logs
	private final String TAG = "xGloveGesture";
	
    private xGloveSensor sensor; //reference to the sensor
    
    public xGloveGesture() 
    {
        this.sensor = xGloveDispatcher.getSensor();
    }

    public Orientation getOrientation() 
    {
        return sensor.getOrientation();
    }

    public boolean fist() 
    {
        return sensor.allFingersBent(66);
    }

    public boolean fingersBent() 
    {
        return sensor.allFingersBent(50);
    }

    public boolean fingersSpread() 
    {
        return !sensor.allFingersBent(50);
    }


    //Consider implementing this with a Timer instead. Or just make this event a non-delayed event
    public boolean upsideDown()
    {
    	if(Math.abs(sensor.getOrientation().roll) > 170)
        {
            /* This for loop is used to ensure that the glove is upside down */
            /* for at least .5 second -> 10 * 0.05 second                    */
            /* This is to avoid measurement errors.                          */         
            for(int i = 0; i < 10; i+=1)
            {
            	//break if sensor is back in original position
                if(Math.abs(sensor.getOrientation().roll) < 115) return false;

                //Delay
                xGloveDispatcher.threadSleep(50);                             
            }    
            return true;  
        }
        return false;    
    }
    
    public boolean rightSideUp()
    {
        return Math.abs(sensor.getOrientation().roll) < 40;
    } 

    // TODO: gesture for spacebar
    public boolean isSpacebarGesture() 
    {
        if(fist() && Math.abs(sensor.getOrientation().pitch) < 30) return true;
        return false;   
    } 
    
    public boolean isSpacebarReleaseGesture()
    {
    	if(!sensor.allFingersBent(55)) return true;
    	return false;
    }

    public boolean isMouseClickGesture(boolean currentlyClicked) 
    {
    	return (sensor.fourFingersBent(65) && (!currentlyClicked) && Math.abs(sensor.getOrientation().pitch) < 35); 
    }
    
    public boolean isDragMouseGesture()
    {
    	System.out.println("ring bent "+ (sensor.isFingerBent(Finger.RING, 72)));
    	System.out.println("middle bent "+ (sensor.isFingerBent(Finger.MIDDLE, 75)));
    	System.out.println("index not bent "+ (!sensor.isFingerBent(Finger.INDEX, 50)));
    	return (sensor.isFingerBent(Finger.RING, 72) && sensor.isFingerBent(Finger.MIDDLE, 75) && (!sensor.isFingerBent(Finger.INDEX, 50)));
    }
    
    public boolean isDragReleaseGesture()
    {
    	System.out.println("ring release "+ (!sensor.isFingerBent(Finger.RING, 63)));
    	System.out.println("middle release "+ (!sensor.isFingerBent(Finger.MIDDLE, 63)));
    	System.out.println("index bent -> release "+ sensor.isFingerBent(Finger.INDEX, 55));
    	return ((!sensor.isFingerBent(Finger.RING, 63)) || (!sensor.isFingerBent(Finger.MIDDLE, 63)) || sensor.isFingerBent(Finger.INDEX, 55));
    }

    public boolean isMouseReleaseGesture(boolean currentlyClicked) 
    {
    	return currentlyClicked && sensor.allFingersSpread();
    }
    
    public boolean isScrollModeGesture() 
    {
        return sensor.isFingerBent(Finger.RING, 83) && !(sensor.isFingerBent(Finger.MIDDLE, 53)) && !(sensor.isFingerBent(Finger.INDEX, 53));
    }
    
    public boolean isReleaseScrollModeGesture() 
    {
    	return !sensor.isFingerBent(Finger.RING, 75) && (sensor.isFingerBent(Finger.MIDDLE, 45));
    }
    
    public boolean isMouseExitGesture()
    {
    	if(sensor.getOrientation().roll > 50 && sensor.getOrientation().roll < 90 &&
    	   Math.abs(sensor.getOrientation().pitch) < 30)
    	{
    		for(int i = 0; i < 800; i++)
    		{
    			if(sensor.getOrientation().roll < 30 && fist())
    			{
    				while(sensor.allFingersBent(65));
    				return true;
    			}
    			else if(Math.abs(sensor.getOrientation().pitch) > 50) break;
    			xGloveDispatcher.threadSleep(2);    
    		}
    	}
    	return false;
    }
    
    public boolean isLoadNextGesture() 
    {
        return sensor.getOrientation().pitch < -50;
    }
    
    public boolean isLoadPreviousGesture() 
    {
        return sensor.getOrientation().pitch > 70;
    }
    
    public int getInclinationFourFingers() 
    {
        return sensor.getInclinationFourFingers();
    }
}
