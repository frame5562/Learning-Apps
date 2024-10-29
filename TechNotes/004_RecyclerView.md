# Android : RecyclerView in Kotlin

### 1. RecyclerView 개요

RecyclerView는 리스트 형태로 데이터를 표시하는 데 최적화된 Android UI 컴포넌트로, 특히 대량의 데이터나 복잡한 리스트 항목을 효율적으로 관리합니다. 기존의 ListView를 대체하며, 뷰 재사용 및 레이아웃 관리 기능이 뛰어납니다.

#### 주요 컴포넌트

- **Adapter**:  
  데이터를 RecyclerView에 연결하는 브릿지 역할을 하며, 각 아이템의 ViewHolder를 생성하고 데이터를 바인딩하는 메서드를 포함합니다.

- **ViewHolder**:  
  RecyclerView의 개별 아이템 뷰를 재사용하며, findViewById를 최소화하여 성능을 높입니다.

- **LayoutManager**:  
  RecyclerView의 레이아웃 형식을 관리합니다. 대표적인 형식으로는 `LinearLayoutManager`, `GridLayoutManager`, `StaggeredGridLayoutManager`가 있습니다.

---

### 2. RecyclerView 구현 단계

#### Step 1: 레이아웃에 RecyclerView 추가

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
/>
```

#### Step 2: 데이터 클래스 정의

```kotlin
data class Item(val title: String, val description: String)
```

#### Step 3: Adapter 및 ViewHolder 생성

```kotlin
class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
    }

    override fun getItemCount() = itemList.size
}
```

#### Step 4: Activity 또는 Fragment에서 Adapter와 LayoutManager 설정

```kotlin
val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
recyclerView.layoutManager = LinearLayoutManager(this)
recyclerView.adapter = ItemAdapter(itemList)
```

---

### 3. RecyclerView의 주요 기능

- **아이템 클릭 이벤트 처리**:  
  ViewHolder에서 `setOnClickListener`를 사용해 개별 아이템의 클릭 이벤트를 처리할 수 있습니다.

- **DiffUtil을 사용한 데이터 변경 효율성**:  
  `DiffUtil`을 통해 데이터 변경 사항을 효율적으로 반영하여 전체 리사이클링 성능을 높일 수 있습니다.

- **ViewType을 이용한 다양한 레이아웃 지원**:  
  `getItemViewType` 메서드를 오버라이드하여 RecyclerView에 서로 다른 레이아웃의 아이템을 추가할 수 있습니다.

---

### 4. RecyclerView 성능 최적화

- **RecyclerView Pool 사용**:  
  여러 RecyclerView가 중첩된 경우 ViewHolder를 재사용하여 메모리 사용량을 줄입니다.

- **ViewHolder 패턴 활용**:  
  findViewById 호출을 최소화하고, 데이터 바인딩을 통해 데이터와 UI를 효율적으로 연결합니다.

---

### 참고 자료

[Android 공식 문서 - RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=ko)

---
<sub>작성일: 2024년 10월 29일</sub>