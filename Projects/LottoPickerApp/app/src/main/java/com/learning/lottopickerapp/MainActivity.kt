package com.learning.lottopickerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearBtn: Button by lazy {
        findViewById<Button>(R.id.btnClear)
    }
    private val addBtn: Button by lazy {
        findViewById<Button>(R.id.btnAdd)
    }
    private val randomAddBtn: Button by lazy {
        findViewById<Button>(R.id.btnRandomAdd)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }
    private val pickNumberSet = hashSetOf<Int>()

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6)
        )
    }

    private var didRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initRandomButton()
        initAddButton()
        initClearButton()
    }

    // NumberPicker 의 최소/최대 값을 설정한다. (로또번호 숫자 제한)
    private fun initNumberPicker() {
        numberPicker.minValue = 1
        numberPicker.maxValue = 45
    }

    // 랜덤으로 번호 추출하는 기능 정의
    private fun initRandomButton() {
        randomAddBtn.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }
        }
    }

    // 수동 추가 버튼 기능 정의
    private fun initAddButton() {
        addBtn.setOnClickListener {

            // 자동 추첨을 통해 모든 숫자가 뽑힌 경우
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 수동으로 번호를 모두 선택한 경우
            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 중보된 번호를 선택 하려고 하는 경우
            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // 그 외 : n 번째 해당 하는 값을 활성화 시키고, 사용자가 선택한 numberPicker 의 숫자 값을 가져온다.
            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }

    }

    // 숫자표시하기: 각 숫자 범위별 색상 지정 (Lotto 와 동일)
    private fun setNumberBackground(number:Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)

        }
    }

    // 초기화 버튼 기능 정의 : 모든 번호를 초기화하고, 화면에서도 보이지 않게 설정한다.
    private fun initClearButton() {
        clearBtn.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }

            didRun = false
        }

    }


    // 무작위 랜덤 숫자를 뽑아내는 코드
    private fun getRandomNumber(): List<Int> {
        // numberList 라는 리스트변수에 1~45 까지 값을 삽입 합니다.
        // 단, 이때 이미 피커를 통해 선택된 숫자와 동일한 값이 존재할 경우 건너 뜁니다.
        // 결과적으로 numberList 에는 선택된 값을 제외한 나머지 숫자들이 존재하게 됩니다.
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }

        // 그렇게 얻은 값들을 랜덤으로 섞어줍니다.
        numberList.shuffle()

        // 새로운 리스트를 생성합니다.
        // 기존에 사용자가 선택한 값 + 랜덤섞기로 얻은 값을 담아 반환합니다.
        // 이때, 셔플된 리스트에서 가져오는 숫자의 개수는 사용자가 선택한 수를 제외한 나머지 만큼만 가져옵니다.
        // 예를 들어 사용자가 2개의 번호를 선택했다면, 로또 번호는 6개의 번호만 필요하기 때문에 4개의 값만 리스트의 앞에서부터 잘라서 가져옵니다.
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        // 반환 값은 정렬된 형태로 돌려주도록 합니다.
        // 로또 번호는 뽑힌 순서에 상관 없이, 작은 순서대로 보여줍니다.
        return newList.sorted()
    }
}