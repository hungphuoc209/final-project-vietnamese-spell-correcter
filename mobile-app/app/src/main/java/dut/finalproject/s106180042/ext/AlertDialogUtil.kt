package dut.finalproject.s106180042.ext

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import dut.finalproject.s106180042.R
import dut.finalproject.s106180042.databinding.ConfirmDialogBinding

class AlertDialogUtil : DialogFragment() {
    lateinit var binding: ConfirmDialogBinding

    companion object {
        fun newInstance(
            title: String,
            message: String,
            positiveBtnText: String,
            onPositiveBtnClick: () -> Unit = {}
        ): AlertDialogUtil {
            val dialog = AlertDialogUtil()
            dialog.apply {
                this.title = title
                this.message = message
                this.positiveBtnText = positiveBtnText
                this.onPositiveBtnClick = onPositiveBtnClick
            }
            return dialog
        }
    }

    private var title: String = ""
    private var message: String = ""
    private var positiveBtnText: String = ""
    private var onPositiveBtnClick: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ConfirmDialogBinding.inflate(inflater, container, false)
        dialog!!.setCancelable(false)

        val windows = dialog!!.window
        windows?.setGravity(Gravity.CENTER)
        val params: WindowManager.LayoutParams? = windows?.attributes

        windows?.attributes = params
        windows?.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_round_corner))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            tvTitle.text = title
            tvMessage.text = message
            btnApply.text = positiveBtnText
            btnCancel.text = resources.getString(R.string.cancel)
            btnApply.setOnClickListener {
                dismiss()
                onPositiveBtnClick.invoke()
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

}

