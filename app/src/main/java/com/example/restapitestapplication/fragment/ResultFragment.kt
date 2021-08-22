package com.example.restapitestapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.restapitestapplication.R
import kotlinx.android.synthetic.main.result_fragment.*
import java.util.ArrayList


class ResultFragment : Fragment() {
    fun newInstance(data: List<String>?): ResultFragment {
        val fragment = ResultFragment()
        // Bundle にパラメータを設定
        val bundle = Bundle()
        bundle.putStringArrayList("ResultList", data as ArrayList<String>?)
        fragment.setArguments(bundle)
        return fragment
    }

    // FragmentのViewを生成して返す
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        if (args != null) {
            val list = args.getStringArrayList("ResultList")
            if (list!!.isNotEmpty()) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list as MutableList<String>)
                result_list.adapter = adapter
                no_result.visibility = View.GONE
                result_list.visibility = View.VISIBLE
            }
            super.onViewCreated(view, savedInstanceState)
        }
    }
}