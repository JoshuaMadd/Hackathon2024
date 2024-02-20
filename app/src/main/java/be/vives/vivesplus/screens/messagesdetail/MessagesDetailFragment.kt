package be.vives.vivesplus.screens.messagesdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentMessagesDetailBinding
import java.util.*


class MessagesDetailFragment : Fragment() {

    lateinit var binding: FragmentMessagesDetailBinding

    @SuppressLint("SetTextI18n", "StringFormatMatches")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages_detail, container, false)
        val args =  MessagesDetailFragmentArgs.fromBundle(this.requireArguments())
        val message = args.message
        val categoryIcon = args.message.categorieIcon.replace("-", "_").plus("_solid")

        val id = requireContext().resources.getIdentifier(categoryIcon, "string", requireContext().packageName)
        binding.detailCategoryIcon.text = getString(id)
        val link = message.link

        val startEvent = message.startEventDate

        val endEvent = message.endEventDate

        val eventLocation = message.location

        if(startEvent != null)
        {
            binding.eventLocation.text = eventLocation

            if(startEvent.minute < 9 && endEvent?.minute!! < 9)
            {
                binding.eventTime.text = startEvent.month.toString().take(3).plus(".")
                    .lowercase(Locale.ROOT)
                    .plus(" ").plus(startEvent.dayOfMonth).plus(" ").plus(startEvent.hour).plus(":").plus("0").plus(startEvent.minute).plus(" - ").plus(
                    endEvent.hour
                ).plus(":").plus("0").plus(
                    endEvent.minute
                )
            }else if(startEvent.minute < 9)
            {

                binding.eventTime.text = startEvent.month.toString().take(3).plus(".")
                    .lowercase(Locale.ROOT)
                    .plus(" ").plus(startEvent.dayOfMonth).plus(" ").plus(startEvent.hour).plus(":").plus("0").plus(startEvent.minute).plus(" - ").plus(endEvent?.hour).plus(":").plus(endEvent?.minute)
            } else if(endEvent?.minute!! < 9)
            {
                binding.eventTime.text = startEvent.month.toString().take(3).plus(".").lowercase(
                    Locale.ROOT
                ).plus(" ").plus(startEvent.dayOfMonth).plus(" ").plus(startEvent.hour).plus(":").plus(startEvent.minute).plus(" - ").plus(
                    endEvent.hour
                ).plus(":").plus("0").plus(
                    endEvent.minute
                )

            }

            binding.layoutSchedule.visibility = View.VISIBLE


        }else{
            binding.layoutSchedule.visibility = View.GONE

            val param = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

            param.setMargins(40,300,40,0)

            binding.detailBeschrijving.layoutParams = param
        }


        if (link != null) {
            if (!link.contains("null")) {
                binding.btnMoreText.visibility = View.VISIBLE
                val buttonText = message.textButtonMore
                binding.btnMoreText.text = buttonText
                binding.btnMoreText.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(i)
                }
            }

            if(link.isEmpty())
            {
                binding.btnMoreText.visibility = View.GONE

            }
        }


        if(message.viewDate.minute == 0)
        {
            binding.detailCreatietijd.text =  message.viewDate.dayOfMonth.toString().plus(" ").plus(
                message.viewDate.month.name.take(3).lowercase(Locale.ROOT)
            ).plus(".").plus(" ").plus(message.viewDate.hour.toString().plus(":").plus(message.viewDate.minute)).plus("0")

        }else {
            binding.detailCreatietijd.text =  message.viewDate.dayOfMonth.toString().plus(" ").plus(
                message.viewDate.month.name.take(3).lowercase(Locale.ROOT)
            ).plus(".").plus(" ").plus(message.viewDate.hour.toString().plus(":").plus(message.viewDate.minute))

        }



        binding.detailCategory.text = message.categorieBeschrijving
        binding.detailVerstuurder.text = message.sender
        binding.detailOnderwerp.text = message.subject
        binding.detailBeschrijving.text = message.description


        return binding.root
    }

}