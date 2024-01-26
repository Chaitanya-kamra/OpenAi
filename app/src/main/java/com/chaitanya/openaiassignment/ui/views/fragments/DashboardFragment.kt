package com.chaitanya.openaiassignment.ui.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chaitanya.openaiassignment.databinding.FragmentDashboardBinding
import com.chaitanya.openaiassignment.domain.model.ApiResponse
import com.chaitanya.openaiassignment.ui.viewModel.MainViewModel
import com.chaitanya.openaiassignment.ui.views.adapter.DashboardLinksAdapter
import com.chaitanya.openaiassignment.utils.DashboardLinkTabState
import com.chaitanya.openaiassignment.utils.DataHandler
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: DashboardLinksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dashboardData.observe(viewLifecycleOwner) { dataHandler ->
            when (dataHandler) {
                is DataHandler.ERROR -> {
                    binding.apply {
                        lnlDashError.visibility = View.VISIBLE
                        pbDashLoading.visibility = View.GONE
                        lnldashMain.visibility = View.GONE
                    }
                }

                is DataHandler.LOADING -> {

                    binding.apply {
                        lnlDashError.visibility = View.GONE
                        pbDashLoading.visibility = View.VISIBLE
                        lnldashMain.visibility = View.GONE
                    }

                }

                is DataHandler.SUCCESS -> {
                    val data = dataHandler.data
                    if (data != null) {
                        binding.apply {
                            lnlDashError.visibility = View.GONE
                            pbDashLoading.visibility = View.GONE
                            lnldashMain.visibility = View.VISIBLE
                        }
                        setUpData(data)
                        setUpChart(data.data.overall_url_chart)
                    }
                }
            }
        }


        viewModel.fetchData()
        binding.tvGreetings.setGreetingMessage()
        binding.btDashError.setOnClickListener {
            viewModel.fetchData()
        }

    }

    private fun TextView.setGreetingMessage() {
        val calendar = Calendar.getInstance()
        this.text = when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..5 -> "Good night"
            in 6..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "good Night"
        }
    }

    private fun setUpData(data: ApiResponse) {
        binding.apply {
            tvTotalClicksText.text = data.total_clicks.toString()
            tvMainLocationText.text = data.top_location
            tvMainSourceText.text = data.top_source
        }
        adapter = DashboardLinksAdapter {
            val clipboardManager =
                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copied Link", it)
            clipboardManager.setPrimaryClip(clipData)
        }
        binding.rvLinks.adapter = adapter
        when (viewModel.dashboardLinkTabState) {
            DashboardLinkTabState.TOP -> {
                binding.rgLinks.check(binding.rbTopLinks.id)
                adapter.submitList(data.data.top_links)
            }

            DashboardLinkTabState.RECENT -> {
                binding.rgLinks.check(binding.rbRecent.id)
                adapter.submitList(data.data.recent_links)
            }

            DashboardLinkTabState.SEARCH -> {

            }
        }
        binding.tvWhatsap.setOnClickListener {
            try {
                val url = "https://api.whatsapp.com/send?phone=${data.support_whatsapp_number}"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).setPackage("com.whatsapp"))
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }

        binding.rgLinks.setOnCheckedChangeListener { radioGroup, id ->
            if (id == binding.rbTopLinks.id) {
                viewModel.changeDashboardLinkTabState(DashboardLinkTabState.TOP)
                adapter.submitList(data.data.top_links)

            } else if (id == binding.rbRecent.id) {
                viewModel.changeDashboardLinkTabState(DashboardLinkTabState.RECENT)
                adapter.submitList(data.data.recent_links)
            }
        }
    }

    private fun setUpChart(overallUrlChart: Map<String, Int>) {
        val lineValues = ArrayList<Entry>()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = dateFormat.parse(overallUrlChart.keys.min())
        val endDate = dateFormat.parse(overallUrlChart.keys.max())
        val textFormat = SimpleDateFormat("d MMM", Locale.US)
        val dateText = textFormat.format(startDate) + "-" + textFormat.format(endDate)
        binding.tvDate.text = dateText

        overallUrlChart.forEach { (key, value) ->
            val date = dateFormat.parse(key)
            if (date != null) {
                lineValues.add(Entry(date.time.toFloat(), value.toFloat()))
            }
        }
        val lineDataSet = LineDataSet(lineValues, null)
        lineDataSet.color = Color.parseColor("#0E6FFF")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        val startColor = Color.parseColor("#0E6FFF")
        val endColor = Color.TRANSPARENT
        val gradientFill = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColor)
        )
        lineDataSet.fillDrawable = gradientFill
        val lineData = LineData(lineDataSet)
        val xAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                return textFormat.format(value.toLong())
            }
        }
        binding.chart.description.isEnabled = false
        binding.chart.legend.isEnabled = false
        binding.chart.axisRight.isEnabled = false
        binding.chart.data = lineData
        binding.chart.animateXY(1000, 1000, Easing.EaseInCubic)

    }

}