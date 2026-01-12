package com.example.myapplication.data
import com.example.myapplication.R

val nagoya = listOf(
    // 나고야 시내 핵심
    Place("나고야성", 35.1850, 136.8996, "나고야-시내"),
    Place("사카에", 35.1681, 136.9066, "나고야-시내"),
    Place("오아시스 21", 35.1709, 136.9086, "나고야-시내"),
    Place("나고야 TV 타워", 35.1704, 136.9081, "나고야-시내"),
    Place("아츠타 신궁", 35.1278, 136.9100, "나고야-남부"),
    Place("오스 상점가", 35.1596, 136.9035, "나고야-시내"),
    Place("나고야항 수족관", 35.0906, 136.8836, "나고야-항구"),

    // 근교 1: 이누야마(전통/성)
    Place("이누야마성", 35.3886, 136.9390, "이누야마-근교"),

    // 근교 2: 도요타(산업/자동차)
    Place("도요타 박물관", 35.1769, 137.0471, "도요타-근교"),

    // 교통 거점
    Place("나고야역", 35.1709, 136.8815, "나고야-시내")
)

fun buildNagoyaPlan(length: TripLength): TripPlan {

    val coordMap = nagoya.associateBy({ it.name }, { it.lat to it.lng })

    fun s(name: String, desc: String, imageResId: Int): SpotDetail {
        val (lat, lng) = coordMap[name]
            ?: error("좌표가 없습니다: $name (nagoya 리스트에 같은 이름 Place를 추가하세요)")
        return SpotDetail(name = name, description = desc, imageResId = imageResId, lat = lat, lng = lng)
    }

    val days = when (length) {

        TripLength.D3_4 -> listOf(
            DayPlan(1, listOf(
                s("나고야성",
                    "나고야를 대표하는 역사 명소로, 웅장한 성곽이 도시의 상징처럼 자리하고 있습니다.\n성 주변을 걷다 보면 여행의 시작이 자연스럽게 정리되는 느낌을 받으실 수 있습니다.",
                    R.drawable.nagoya_castle),
                s("사카에·오아시스21",
                    "나고야 도심의 중심지로, 쇼핑과 야경이 자연스럽게 이어지는 세련된 거리입니다.\n유리 구조물과 빛이 어우러진 풍경이 밤에 특히 인상적입니다.",
                    R.drawable.nagoya_oasis21),
                s("미라이 타워(나고야 TV 타워)",
                    "사카에의 랜드마크로, 도심 분위기를 가장 직관적으로 느낄 수 있는 전망 포인트입니다.\n불빛이 켜진 거리 위에서 나고야의 밤을 차분히 내려다볼 수 있습니다.",
                    R.drawable.nagoya_tv_tower)
            )),
            DayPlan(2, listOf(
                s("아쓰타 신궁",
                    "일본에서도 손꼽히는 유서 깊은 신궁으로, 고요한 숲길이 인상적인 곳입니다.\n참배길을 따라 걷는 시간 자체가 여행의 속도를 부드럽게 낮춰줍니다.",
                    R.drawable.nagoya_atsuta),
                s("오스 상점가",
                    "로컬 먹거리와 개성 있는 가게들이 이어지는 활기찬 거리입니다.\n천천히 둘러보며 나고야의 일상과 분위기를 가까이에서 느끼실 수 있습니다.",
                    R.drawable.nagoya_osu),
                s("나고야항 수족관",
                    "바다와 맞닿은 공간에 위치한 대형 수족관으로, 여유롭게 둘러보기 좋습니다.\n도심 일정과는 다른 결의 휴식을 느낄 수 있는 장소입니다.",
                    R.drawable.nagoya_aquarium)
            )),
            DayPlan(3, listOf(
                s("SCMAGLEV·철도관",
                    "일본 철도 기술을 직접 체험하며 볼 수 있는 박물관으로, 전시 자체가 매우 흥미롭습니다.\n관람을 마치고 나면 이 도시의 산업적 색깔이 더욱 또렷하게 느껴집니다.",
                    R.drawable.nagoya_sc_maglev),
                s("레고랜드 재팬",
                    "레고를 테마로 한 체험형 테마파크로, 밝고 경쾌한 분위기가 특징입니다.\n가볍게 즐기며 하루를 보내기에 좋은 코스입니다.",
                    R.drawable.nagoya_legoland)
            ))
        )

        TripLength.D4_5 -> listOf(
            DayPlan(1, listOf(
                s("나고야성",
                    "나고야의 상징 같은 장소로, 성을 중심으로 도시의 분위기를 한 번에 느낄 수 있습니다.\n첫날 방문지로 가장 잘 어울리는 명소입니다.",
                    R.drawable.nagoya_castle),
                s("사카에",
                    "도심의 쇼핑과 카페, 야경이 이어지는 나고야의 중심 거리입니다.\n밤에 걸으면 도시의 리듬이 더욱 또렷하게 느껴집니다.",
                    R.drawable.nagoya_sakae)
            )),
            DayPlan(2, listOf(
                s("아쓰타 신궁",
                    "고요한 숲과 깊은 전통이 함께 어우러진 신궁입니다.\n여행 중간에 들르면 마음을 차분하게 정리하기 좋습니다.",
                    R.drawable.nagoya_atsuta),
                s("오스 상점가",
                    "먹거리와 소품 가게가 이어지는 아케이드 거리입니다.\n걷는 것만으로도 나고야의 생활감이 자연스럽게 전해집니다.",
                    R.drawable.nagoya_osu)
            )),
            DayPlan(3, listOf(
                s("SCMAGLEV·철도관",
                    "철도와 기술을 좋아하신다면 가장 몰입감 있게 즐길 수 있는 장소입니다.\n체험형 전시가 많아 시간이 빠르게 지나갑니다.",
                    R.drawable.nagoya_sc_maglev),
                s("나고야항 수족관",
                    "항구 분위기와 함께 여유롭게 둘러보기 좋은 수족관입니다.\n하루 일정의 흐름을 부드럽게 마무리하기 좋습니다.",
                    R.drawable.nagoya_aquarium)
            )),
            DayPlan(4, listOf(
                s("레고랜드 재팬",
                    "밝고 경쾌한 분위기의 테마파크로, 색다른 하루를 보내기 좋습니다.\n사진을 남기기에도 좋은 포인트가 많습니다.",
                    R.drawable.nagoya_legoland)
            )),
            DayPlan(5, listOf(
                s("사카에(마무리 산책)",
                    "여행 마지막 날, 부담 없이 쇼핑과 산책으로 마무리하기 좋은 도심입니다.\n도시의 분위기를 느끼며 천천히 여행을 정리할 수 있습니다.",
                    R.drawable.nagoya_sakae)
            ))
        )

        TripLength.D5_6 -> listOf(
            DayPlan(1, listOf(
                s("나고야성",
                    "나고야의 분위기를 가장 잘 보여주는 대표 명소입니다.\n성 주변을 걷는 것만으로도 도시의 결이 전해집니다.",
                    R.drawable.nagoya_castle),
                s("사카에",
                    "쇼핑과 야경이 이어지는 도심의 중심지입니다.\n밤에 한 번 더 걸어보시면 분위기가 전혀 다르게 느껴집니다.",
                    R.drawable.nagoya_sakae)
            )),
            DayPlan(2, listOf(
                s("아쓰타 신궁",
                    "고요한 숲길과 깊은 전통이 인상적인 신궁입니다.\n여행의 속도를 잠시 낮추기에 가장 잘 어울립니다.",
                    R.drawable.nagoya_atsuta)
            )),
            DayPlan(3, listOf(
                s("오스 상점가",
                    "로컬 감성이 살아 있는 아케이드 거리입니다.\n천천히 걷다 보면 나고야의 일상이 가까이 다가옵니다.",
                    R.drawable.nagoya_osu)
            )),
            DayPlan(4, listOf(
                s("SCMAGLEV·철도관",
                    "일본 철도 기술의 집약을 직접 체험할 수 있는 공간입니다.\n관람 후에는 도시의 성격이 더 분명하게 느껴집니다.",
                    R.drawable.nagoya_sc_maglev)
            )),
            DayPlan(5, listOf(
                s("나고야항 수족관",
                    "도심 일정과 다른 결의 여유를 느낄 수 있는 장소입니다.\n조용히 머무르며 쉬어가기 좋습니다.",
                    R.drawable.nagoya_aquarium),
                s("레고랜드 재팬",
                    "여행 후반에 가볍게 즐기기 좋은 테마 공간입니다.\n밝은 분위기 속에서 하루를 편하게 보내실 수 있습니다.",
                    R.drawable.nagoya_legoland)
            )),
            DayPlan(6, listOf(
                s("사카에(자유 일정)",
                    "마지막 날은 도심에서 여유롭게 시간을 보내기 좋습니다.\n기념품을 고르고 천천히 걷는 것만으로도 충분한 마무리가 됩니다.",
                    R.drawable.nagoya_sakae)
            ))
        )
    }

    return TripPlan(city = City.NAGOYA, length = length, days = days)
}
