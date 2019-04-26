package com.acuant.sampleapp.backgroundtasks

import android.graphics.Bitmap
import android.os.AsyncTask
import com.acuant.acuantcamera.CapturedImage
import com.acuant.acuantcommon.model.Image
import com.acuant.acuantimagepreparation.AcuantImagePreparation
import com.acuant.acuantimagepreparation.model.CroppingData
import com.acuant.acuantimagepreparation.model.CroppingOptions


/**
 * Created by tapasbehera on 4/30/18.
 */
class CroppingTask constructor(val originalImage:Bitmap, val isHealthCard : Boolean, val isFrontImage:Boolean, val listener: CroppingTaskListener) : AsyncTask<String, String, String>() {

    var image : Bitmap? = originalImage
    var isHealthInsuranceCard : Boolean = isHealthCard
    var taskListener : CroppingTaskListener? = listener
    var frontImage : Boolean = isFrontImage
    private var acuantImage : Image? = null

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg p0: String?): String {
        if(image!=null) {
            val options = CroppingOptions()
            options.isHealthCard = isHealthInsuranceCard

            val data = CroppingData()
            data.image = image
            //CommonUtils.saveImage(data.image,"original")
            acuantImage = AcuantImagePreparation.crop(options,data)

            if(acuantImage?.image != null) {
                CapturedImage.sharpnessScore = AcuantImagePreparation.sharpness(acuantImage?.image)
                CapturedImage.glareScore = AcuantImagePreparation.glare(acuantImage?.image)
            }

        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(taskListener!=null){
            taskListener!!.croppingFinished(acuantImage,frontImage)
        }
    }
}