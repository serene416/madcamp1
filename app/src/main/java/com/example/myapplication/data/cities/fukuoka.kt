package com.example.myapplication.data.cities
import com.example.myapplication.R
import com.example.myapplication.data.City
import com.example.myapplication.data.DayPlan
import com.example.myapplication.data.Place
import com.example.myapplication.data.SpotDetail
import com.example.myapplication.data.TripLength
import com.example.myapplication.data.TripPlan

val fukuoka = listOf(
    // 후쿠오카 시내(하카타/텐진 중심)
    Place("캐널시티 하카타", 33.5894, 130.4106, "후쿠오카-하카타"),
    Place("하카타역", 33.5902, 130.4206, "후쿠오카-하카타"),
    Place("쿠시다 신사", 33.5928, 130.4105, "후쿠오카-하카타"),
    Place("텐진 지하상가", 33.5902, 130.3986, "후쿠오카-텐진"),
    Place("오호리 공원", 33.5863, 130.3776, "후쿠오카-서부"),
    Place("후쿠오카 타워", 33.5931, 130.3519, "후쿠오카-서부"),
    Place("모모치 해변", 33.5946, 130.3496, "후쿠오카-서부"),

    // 당일치기/근교(선택)
    Place("다자이후 텐만구", 33.5215, 130.5341, "후쿠오카-근교"),

    // 온천 핵심: 유후인(오이타현)
    Place("유후인역", 33.2634, 131.3236, "유후인-온천"),
    Place("긴린코 호수", 33.2666, 131.3126, "유후인-온천"),
    Place("하카타역·JR 하카타시티", 33.5902, 130.4206, "후쿠오카-하카타"),
    Place("나카스 야타이 거리", 33.5941, 130.4088, "후쿠오카-하카타"),
    Place("유후인", 33.2634, 131.3236, "유후인-온천"),
)




fun buildFukuokaPlan(length: TripLength): TripPlan {

    val coordMap = fukuoka.associateBy({ it.name }, { it.lat to it.lng })

    fun s(name: String, desc: String, imageResId: Int): SpotDetail {
        val (lat, lng) = coordMap[name]
            ?: error("좌표가 없습니다: $name (fukuoka 리스트에 같은 이름 Place를 추가하세요)")
        return SpotDetail(
            name = name,
            description = desc,
            imageResId = imageResId,
            lat = lat,
            lng = lng
        )
    }

    val days = when (length) {

        TripLength.D3_4 -> listOf(
            DayPlan(
                1, listOf(
                    s(
                        "하카타역·JR 하카타시티",
                        "후쿠오카 여행의 출발점이 되는 중심지로, 쇼핑과 식사가 한 번에 해결되는 공간입니다.\n역 주변을 걷다 보면 도시의 속도와 여행의 리듬이 자연스럽게 맞춰집니다.",
                        R.drawable.fukuoka_hakata
                    ),
                    s(
                        "캐널시티 하카타",
                        "운하를 품은 대형 복합 쇼핑몰로, 산책하듯 둘러보기 좋은 후쿠오카 대표 명소입니다.\n물길과 조명이 어우러진 공간에서 쇼핑과 휴식을 동시에 즐기실 수 있습니다.",
                        R.drawable.fukuoka_canal_city
                    ),
                    s(
                        "나카스 야타이 거리",
                        "강변을 따라 포장마차가 이어지는 곳으로, 후쿠오카의 밤을 가장 후쿠오카답게 느낄 수 있습니다.\n따뜻한 라멘 한 그릇과 사람들의 온기가 여행의 기억을 깊게 남겨줍니다.",
                        R.drawable.fukuoka_nakasu
                    )
                )
            ),
            DayPlan(
                2, listOf(
                    s(
                        "후쿠오카 타워",
                        "도시와 바다를 한눈에 담을 수 있는 전망 명소로, 후쿠오카의 구조를 가장 잘 보여줍니다.\n노을부터 야경까지 이어지는 풍경이 하루를 차분하게 마무리해줍니다.",
                        R.drawable.fukuoka_tower
                    ),
                    s(
                        "모모치 해변",
                        "도심에서 가까운 해변 산책 코스로, 바다와 도시가 맞닿은 후쿠오카의 매력을 느낄 수 있습니다.\n바닷바람을 맞으며 걷다 보면 여행의 피로가 자연스럽게 풀립니다.",
                        R.drawable.fukuoka_momochi
                    )
                )
            ),
            DayPlan(
                3, listOf(
                    s(
                        "유후인",
                        "온천 마을 특유의 여유로운 분위기와 자연 풍경이 인상적인 대표 근교 여행지입니다.\n하카타에서 특급 열차로 이동해 하루를 온전히 보내기에 가장 적합한 코스입니다.",
                        R.drawable.fukuoka_yufuin
                    ),
                    s(
                        "긴린코 호수",
                        "유후인을 대표하는 고요한 호수로, 아침이나 오후 어느 시간에 가도 분위기가 좋습니다.\n잔잔한 물결과 주변 풍경이 여행의 여운을 깊게 남겨줍니다.",
                        R.drawable.fukuoka_kinrinko
                    )
                )
            )

        )

        TripLength.D4_5 -> listOf(
            DayPlan(
                1, listOf(
                    s(
                        "하카타역·JR 하카타시티",
                        "도착 직후 동선을 잡기 좋은 후쿠오카의 중심지입니다.\n쇼핑과 식사를 부담 없이 시작하기 좋습니다.",
                        R.drawable.fukuoka_hakata
                    ),
                    s(
                        "캐널시티 하카타",
                        "운하를 중심으로 공연과 상점이 이어지는 복합 공간입니다.\n시간대에 따라 분위기가 달라 머무는 재미가 있습니다.",
                        R.drawable.fukuoka_canal_city
                    )
                )
            ),
            DayPlan(
                2, listOf(
                    s(
                        "후쿠오카 타워",
                        "도시와 바다를 함께 조망할 수 있는 대표 전망 명소입니다.\n해 질 무렵에 오르면 풍경의 변화가 특히 인상적입니다.",
                        R.drawable.fukuoka_tower
                    ),
                    s(
                        "모모치 해변",
                        "전망대를 내려와 이어서 산책하기 좋은 해변 코스입니다.\n바닷바람과 함께 하루를 부드럽게 정리하기 좋습니다.",
                        R.drawable.fukuoka_momochi
                    )
                )
            ),
            DayPlan(
                3, listOf(
                    s(
                        "다자이후 텐만구",
                        "후쿠오카 근교에서 가장 유명한 전통 참배 명소입니다.\n고즈넉한 분위기 속에서 여행의 흐름이 차분해집니다.",
                        R.drawable.fukuoka_dazaifu
                    )
                )
            ),
            DayPlan(
                4, listOf(
                    s(
                        "유후인",
                        "후쿠오카에서 가장 인기 있는 근교 여행 코스입니다.\n자연 풍경과 온천 분위기가 여행의 색을 크게 바꿔줍니다.",
                        R.drawable.fukuoka_yufuin
                    )
                )
            ),
            DayPlan(
                5, listOf(
                    s(
                        "나카스 야타이 거리",
                        "여행의 마지막 밤을 보내기 가장 좋은 포장마차 거리입니다.\n따뜻한 음식과 활기 속에서 후쿠오카다운 마무리를 할 수 있습니다.",
                        R.drawable.fukuoka_nakasu
                    )
                )
            )
        )

        TripLength.D5_6 -> listOf(
            DayPlan(
                1, listOf(
                    s(
                        "하카타역·JR 하카타시티",
                        "후쿠오카 여행의 출발점이 되는 중심지입니다.\n도착 직후에도 부담 없이 여행을 시작할 수 있습니다.",
                        R.drawable.fukuoka_hakata
                    ),
                    s(
                        "캐널시티 하카타",
                        "운하를 품은 복합 공간으로 걷는 것만으로도 즐거운 코스입니다.\n밤에는 조명과 분위기가 특히 아름답습니다.",
                        R.drawable.fukuoka_canal_city
                    )
                )
            ),
            DayPlan(
                2, listOf(
                    s(
                        "후쿠오카 타워",
                        "도시와 바다를 한 번에 담을 수 있는 전망 명소입니다.\n노을과 야경이 이어지는 시간대가 가장 인상적입니다.",
                        R.drawable.fukuoka_tower
                    ),
                    s(
                        "모모치 해변",
                        "도심과 가까운 해변 산책 코스로 여유를 느끼기 좋습니다.\n바닷바람 속에서 여행의 피로가 자연스럽게 풀립니다.",
                        R.drawable.fukuoka_momochi
                    )
                )
            ),
            DayPlan(
                3, listOf(
                    s(
                        "다자이후 텐만구",
                        "전통적인 분위기와 참배길이 인상적인 근교 명소입니다.\n여행의 결을 차분하게 만들어주는 공간입니다.",
                        R.drawable.fukuoka_dazaifu
                    )
                )
            ),
            DayPlan(
                4, listOf(
                    s(
                        "유후인",
                        "자연과 온천 분위기를 함께 느낄 수 있는 대표 근교 코스입니다.\n하루만 다녀와도 여행의 분위기가 크게 바뀝니다.",
                        R.drawable.fukuoka_yufuin
                    )
                )
            ),
            DayPlan(
                5, listOf(
                    s(
                        "나카스 야타이 거리",
                        "후쿠오카의 밤을 가장 후쿠오카답게 즐길 수 있는 장소입니다.\n따뜻한 음식과 사람들 속에서 여행을 정리하기 좋습니다.",
                        R.drawable.fukuoka_nakasu
                    )
                )
            ),
            DayPlan(
                6, listOf(
                    s(
                        "캐널시티 하카타",
                        "여행 마지막 날, 가볍게 산책하며 들르기 좋은 공간입니다.\n머무는 동안의 기억을 천천히 정리하기 좋습니다.",
                        R.drawable.fukuoka_canal_city
                    )
                )
            )
        )
    }

    return TripPlan(city = City.FUKUOKA, length = length, days = days)
}
