package com.example.myapplication.ui.tab1

val firstTabTiePriority = listOf(
    Region.TOKYO,
    Region.OSAKA,
    Region.FUKUOKA,
    Region.SAPPORO,
    Region.NAGOYA
)

fun regionKoreanName(r: Region): String = when (r) {
    Region.TOKYO -> "도쿄"
    Region.OSAKA -> "오사카"
    Region.FUKUOKA -> "후쿠오카"
    Region.SAPPORO -> "삿포로"
    Region.NAGOYA -> "나고야"
}

/** Q1~Q7만 정의 (RESULT는 별도) */
val firstTabQuestions: List<Question> = listOf(
    Question(
        id = "Q1",
        text = "도시가 좋아? 자연이 좋아?",
        choices = listOf(
            Choice(id = "Q1_A", label = "🏙️ 도시", addRegions = listOf(Region.TOKYO, Region.OSAKA)),
            Choice(id = "Q1_B", label = "🌿 자연", addRegions = listOf(Region.FUKUOKA, Region.SAPPORO, Region.NAGOYA))
        )
    ),
    Question(
        id = "Q2",
        text = "온천 여행 좋아해?♨️",
        choices = listOf(
            Choice(id = "Q2_A", label = "♨️ 좋아", addRegions = listOf(Region.FUKUOKA)),
            Choice(id = "Q2_B", label = "❌ 싫어", addRegions = emptyList())
        )
    ),
    Question(
        id = "Q3",
        text = "눈 좋아해?❄️",
        choices = listOf(
            Choice(id = "Q3_A", label = "❄️ 좋아", addRegions = listOf(Region.SAPPORO)),
            Choice(id = "Q3_B", label = "❌ 싫어", addRegions = emptyList())
        )
    ),
    Question(
        id = "Q4",
        text = "하루 종일 쇼핑하는 거 좋아해?🛍️",
        choices = listOf(
            Choice(id = "Q4_A", label = "🛍️ 좋아", addRegions = listOf(Region.OSAKA)),
            Choice(id = "Q4_B", label = "❌ 싫어", addRegions = emptyList())
        )
    ),
    Question(
        id = "Q5",
        text = "절과 사찰의 차분한 분위기 좋아해?",
        choices = listOf(
            Choice(id = "Q5_A", label = "🙏 좋아", addRegions = listOf(Region.NAGOYA)),
            Choice(id = "Q5_B", label = "❌ 싫어", addRegions = emptyList())
        )
    ),
    Question(
        id = "Q6",
        text = "럭셔리한 여행 좋아해?✨",
        choices = listOf(
            Choice(id = "Q6_A", label = "✨ 좋아", addRegions = listOf(Region.TOKYO)),
            Choice(id = "Q6_B", label = "❌ 싫어", addRegions = emptyList())
        )
    ),
    Question(
        id = "Q7",
        text = "사람들이 많이 가는 여행지가 좋아?",
        choices = listOf(
            Choice(id = "Q7_A", label = "👍 좋아", addRegions = emptyList()),
            Choice(id = "Q7_B", label = "🤔 상관없어", addRegions = emptyList())
        )
    )
)
