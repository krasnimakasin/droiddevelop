package kn.outside;

import java.lang.reflect.*;
import android.os.MessageQueue;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;

public class ModalDialog
{
   private boolean mQuitModal =false;     

   private Method mMsgQueueNextMethod =null;
   private Field mMsgTargetFiled =null;
   
   public Boolean startModal()
   {
      if (prepareModal())
         doModal();
      else
         return false;
      return true;
   }
   
   public void closeModal()
   {
       mQuitModal =true;   
   }

   private Boolean prepareModal()
   {
      Class <?> clsMsgQueue =null;
      Class <?> clsMessage =null;

      try
      {
         clsMsgQueue =Class.forName("android.os.MessageQueue");
         clsMessage =Class.forName("android.os.Message");
         mMsgQueueNextMethod = clsMsgQueue.getDeclaredMethod("next",new Class[]{});
         mMsgQueueNextMethod.setAccessible(true);
         mMsgTargetFiled = clsMessage.getDeclaredField("target");
         mMsgTargetFiled.setAccessible(true);
      }
      catch(Exception e)
      {
        return false;
      }   
    return true;
}

private void doModal()
{
    mQuitModal =false;

    // get message queue associated with main UI thread
    MessageQueue queue =Looper.myQueue();
    while(!mQuitModal)
    {
        // call queue.next(), might block
        Message msg =null;
        try
        {
            msg =(Message)mMsgQueueNextMethod.invoke(queue,new Object[]{});
        }
        catch(Exception e)
        {
        }

        if(null != msg)
        {
            Handler target =null;
            try
            {
                target =(Handler)mMsgTargetFiled.get(msg);
            }
            catch(Exception e)
            {
            }
            if(target ==null)
            {
                // No target is a magic identifier for the quit message.
                mQuitModal =true;
            }

            target.dispatchMessage(msg);
            msg.recycle();
        }
    }
}
}
