package com.example.myapplication.data
import com.example.myapplication.R

val sapporo = listOf(
    // 삿포로 시내(중심)
    Place("오도리 공원", 43.0621, 141.3544, "삿포로-시내"),
    Place("삿포로 TV 타워", 43.0612, 141.3560, "삿포로-시내"),
    Place("삿포로 시계탑", 43.0622, 141.3539, "삿포로-시내"),
    Place("삿포로역", 43.0687, 141.3507, "삿포로-시내"),
    Place("스스키노", 43.0553, 141.3546, "삿포로-시내"),
    Place("니조 시장", 43.0577, 141.3608, "삿포로-시내"),
    Place("삿포로 맥주 박물관", 43.0706, 141.3680, "삿포로-시내"),
    Place("모이와야마", 43.0316, 141.3223, "삿포로-서부"),
    Place("오타루", 43.1980, 141.0010, "오타루-근교"),
    Place("모이와 산 로프웨이", 43.0351, 141.3223, "삿포로-서부"),
    Place("오타루 운하", 43.1980, 141.0010, "오타루-근교"),
    Place("오타루 사카이마치 거리", 43.1998, 141.0032, "오타루-근교"),
    Place("조잔케이 온천", 42.9670, 141.1679, "조잔케이-온천"),
    Place("사카에·오아시스21", 35.1709, 136.9086, "나고야-시내"),     // 오아시스21 좌표
    Place("미라이 타워(나고야 TV 타워)", 35.1704, 136.9081, "나고야-시내"), // TV 타워 좌표
    Place("SCMAGLEV·철도관", 35.0969, 136.8506, "나고야-항구"),       // 리니어·철도관(킨조후토 근처 대표값)
    Place("레고랜드 재팬", 35.0387, 136.8473, "나고야-항구"),          // 레고랜드(킨조후토 대표값)
    Place("사카에(마무리 산책)", 35.1681, 136.9066, "나고야-시내"),     // 사카에 좌표
    Place("사카에(자유 일정)", 35.1681, 136.9066, "나고야-시내")     // 사카에 좌표

)





fun buildSapporoPlan(length: TripLength): TripPlan {
    val coordMap = sapporo.associateBy({ it.name }, { it.lat to it.lng })

    fun s(name: String, desc: String, imageResId: Int): SpotDetail {
        val (lat, lng) = coordMap[name]
            ?: error("좌표가 없습니다: $name (sapporo 리스트에 같은 이름 Place를 추가하세요)")
        return SpotDetail(name = name, description = desc, imageResId = imageResId, lat = lat, lng = lng)
    }


    val days = when (length) {

        TripLength.D3_4 -> listOf(
            DayPlan(1, listOf(
                s(
                    "오도리 공원",
                    "삿포로 도심을 길게 가로지르는 공원으로, 도시의 중심에서 계절을 가장 가까이 느낄 수 있는 곳입니다.\n걷는 내내 풍경이 부드럽게 이어져 여행의 첫 리듬을 만들기에 좋습니다.",
                    R.drawable.sapporo_odori
                ),
                s(
                    "삿포로 시계탑",
                    "삿포로를 상징하는 역사 건축물로, 짧게 들러도 도시의 배경이 또렷해지는 장소입니다.\n사진 한 장을 남기는 순간에도 ‘삿포로다움’이 자연스럽게 담깁니다.",
                    R.drawable.sapporo_clock_tower
                ),
                s(
                    "삿포로 TV 타워",
                    "오도리 공원을 한눈에 내려다볼 수 있는 도심 전망 포인트입니다.\n해 질 무렵에 오르면 도시의 불빛이 켜지는 흐름을 차분히 감상하실 수 있습니다.",
                    R.drawable.sapporo_tv_tower
                )
            )),
            DayPlan(2, listOf(
                s(
                    "니조 시장",
                    "해산물과 로컬 먹거리가 모여 있는 시장으로, 삿포로의 식문화를 가장 가까이에서 만날 수 있습니다.\n이른 시간에 방문하시면 시장의 활기와 신선함을 더 생생하게 느끼실 수 있습니다.",
                    R.drawable.sapporo_nijo
                ),
                s(
                    "스스키노",
                    "삿포로의 밤이 가장 선명하게 드러나는 번화가로, 불빛과 사람들의 온기가 거리 전체를 채웁니다.\n늦은 시간까지 이어지는 분위기 속에서 도시의 에너지를 자연스럽게 경험하실 수 있습니다.",
                    R.drawable.sapporo_susukino
                ),
                s(
                    "삿포로 맥주 박물관",
                    "삿포로의 대표 문화를 담은 공간으로, 역사와 시음 분위기가 함께 어우러집니다.\n여행 중간에 들르면 ‘이 도시의 맛’을 기억으로 남기기 좋습니다.",
                    R.drawable.sapporo_beer
                )
            )),
            DayPlan(3, listOf(
                s(
                    "오타루",
                    "삿포로 근교에서 가장 인기 있는 항구 도시로, 거리 자체가 여행의 분위기를 바꿔줍니다.\n천천히 걸으며 바다 공기와 함께 북해도의 감성을 느끼실 수 있습니다.",
                    R.drawable.sapporo_otaru
                ),
                s(
                    "오타루 운하",
                    "오타루를 대표하는 풍경으로, 물길과 창고 건물이 만들어내는 분위기가 인상적입니다.\n해질 무렵부터 조명이 켜지는 시간대에 특히 아름답게 느껴집니다.",
                    R.drawable.sapporo_otaru_canal
                ),
                s(
                    "모이와야마",
                    "삿포로 야경을 가장 아름답게 볼 수 있는 대표 전망 명소입니다.\n정상에서 내려다보는 불빛의 바다로 하루를 완벽하게 마무리하실 수 있습니다.",
                    R.drawable.sapporo_moiwa
                )
            ))
        )

        TripLength.D4_5 -> listOf(
            DayPlan(1, listOf(
                s(
                    "오도리 공원",
                    "도심 한가운데에서 계절의 공기를 느낄 수 있는 삿포로의 대표 산책길입니다.\n여행의 시작을 부드럽게 열어주는 공간입니다.",
                    R.drawable.sapporo_odori
                ),
                s(
                    "삿포로 시계탑",
                    "짧게 들러도 도시의 역사와 분위기가 정리되는 상징적인 장소입니다.\n사진으로 남기기에도 좋은 포인트입니다.",
                    R.drawable.sapporo_clock_tower
                )
            )),
            DayPlan(2, listOf(
                s(
                    "니조 시장",
                    "해산물과 로컬 먹거리가 모여 있는 시장입니다.\n아침에 방문하시면 활기와 신선함을 더 생생하게 느끼실 수 있습니다.",
                    R.drawable.sapporo_nijo
                ),
                s(
                    "삿포로 맥주 박물관",
                    "삿포로의 대표 문화를 담은 공간으로, 역사와 분위기를 함께 즐길 수 있습니다.\n한 잔의 여유로 여행의 기억을 더 깊게 남기기 좋습니다.",
                    R.drawable.sapporo_beer
                )
            )),
            DayPlan(3, listOf(
                s(
                    "오타루",
                    "항구 도시 특유의 감성이 살아 있는 근교 여행지입니다.\n삿포로와는 다른 분위기 속에서 하루의 결이 바뀌는 느낌을 받으실 수 있습니다.",
                    R.drawable.sapporo_otaru
                ),
                s(
                    "오타루 운하",
                    "물길과 창고 건물이 만들어내는 풍경이 인상적인 장소입니다.\n조명이 켜지는 시간대에 산책하면 분위기가 더 깊어집니다.",
                    R.drawable.sapporo_otaru_canal
                )
            )),
            DayPlan(4, listOf(
                s(
                    "스스키노",
                    "삿포로의 밤이 가장 활기차게 펼쳐지는 번화가입니다.\n불빛과 사람들 사이를 걷는 것만으로도 도시의 에너지를 느끼실 수 있습니다.",
                    R.drawable.sapporo_susukino
                ),
                s(
                    "모이와야마",
                    "삿포로 야경을 가장 아름답게 담을 수 있는 전망 명소입니다.\n하루의 마지막을 가장 완벽하게 마무리하기 좋은 코스입니다.",
                    R.drawable.sapporo_moiwa
                )
            )),
            DayPlan(5, listOf(
                s(
                    "삿포로 TV 타워",
                    "오도리 공원과 도심을 한눈에 내려다볼 수 있는 전망 포인트입니다.\n여행 마지막 날, 도시의 형태를 다시 한 번 정리해보기 좋습니다.",
                    R.drawable.sapporo_tv_tower
                )
            ))
        )

        TripLength.D5_6 -> listOf(
            DayPlan(1, listOf(
                s(
                    "오도리 공원",
                    "삿포로 도심을 가로지르는 대표 산책길입니다.\n여행의 첫 리듬을 만들기에 가장 좋은 곳입니다.",
                    R.drawable.sapporo_odori
                ),
                s(
                    "삿포로 시계탑",
                    "도시의 상징 같은 역사 건축물입니다.\n짧게 들러도 삿포로의 분위기가 또렷해집니다.",
                    R.drawable.sapporo_clock_tower
                )
            )),
            DayPlan(2, listOf(
                s(
                    "니조 시장",
                    "삿포로의 식문화를 가까이에서 느낄 수 있는 시장입니다.\n아침 방문이 특히 잘 어울리는 장소입니다.",
                    R.drawable.sapporo_nijo
                )
            )),
            DayPlan(3, listOf(
                s(
                    "오타루",
                    "근교 항구 도시의 감성을 느낄 수 있는 대표 코스입니다.\n천천히 걷다 보면 여행의 분위기가 확 달라집니다.",
                    R.drawable.sapporo_otaru
                ),
                s(
                    "오타루 운하",
                    "오타루를 대표하는 풍경으로, 산책만으로도 충분히 만족스러운 장소입니다.\n해질 무렵과 야간 조명 시간대가 특히 아름답습니다.",
                    R.drawable.sapporo_otaru_canal
                )
            )),
            DayPlan(4, listOf(
                s(
                    "삿포로 맥주 박물관",
                    "삿포로의 이름과 가장 잘 어울리는 공간입니다.\n여행 중간에 들러 한 잔의 여유를 더하기 좋습니다.",
                    R.drawable.sapporo_beer
                )
            )),
            DayPlan(5, listOf(
                s(
                    "스스키노",
                    "삿포로의 밤을 가장 생생하게 느낄 수 있는 번화가입니다.\n불빛과 사람들의 온기 속에서 도시의 에너지가 전해집니다.",
                    R.drawable.sapporo_susukino
                ),
                s(
                    "모이와야마",
                    "삿포로 야경을 가장 아름답게 볼 수 있는 전망 명소입니다.\n정상에서 내려다보는 풍경이 하루를 완벽히 마무리해줍니다.",
                    R.drawable.sapporo_moiwa
                )
            )),
            DayPlan(6, listOf(
                s(
                    "삿포로 TV 타워",
                    "도심과 오도리 공원을 한눈에 담을 수 있는 전망 포인트입니다.\n여행의 끝에서 도시를 다시 한 번 천천히 내려다보기 좋습니다.",
                    R.drawable.sapporo_tv_tower
                )
            ))
        )
    }

    return TripPlan(city = City.SAPPORO, length = length, days = days)
}
