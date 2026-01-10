
package com.example.myapplication.data
import com.example.myapplication.R

val osaka = listOf(
        Place("오사카성", 34.68739, 135.52575, "중앙"),
        Place("도톤보리", 34.6685488, 135.5027158, "난바"),
        Place("신사이바시", 34.6709055, 135.5013367, "난바"),
        Place("우메다 스카이 빌딩", 34.705253, 135.490096, "우메다"),
        Place("USJ", 34.664505, 135.430968, "베이 에리어"),
        Place("카이유칸(해유관)", 34.6527, 135.4240, "베이 에리어"),
        Place("츠텐카쿠", 34.652500, 135.506302, "신세카이"),
        Place("쿠로몬 시장", 34.6646008, 135.5069718, "난바"),
        Place("난바 야사카 신사", 34.661222, 135.496694, "난바"),
        Place("스미요시타이샤", 34.6127972, 135.4929417, "남부")
    )


fun buildOsakaPlan(length: TripLength): TripPlan {
    fun s(name: String, desc: String, imageResId: Int) =
        SpotDetail(name = name, description = desc, imageResId = imageResId)

    val days = when (length) {

        TripLength.D3_4 -> listOf(
            DayPlan(1, listOf(
                s(
                    "도톤보리",
                    "오사카의 활기가 가장 밀집된 거리로, 화려한 간판과 사람들의 에너지가 인상적인 곳입니다.\n강을 따라 걷다 보면 오사카 특유의 자유롭고 유쾌한 분위기를 자연스럽게 느끼실 수 있습니다.",
                    R.drawable.osaka_dotonbori
                ),
                s(
                    "신사이바시",
                    "긴 아케이드 거리로 이어진 오사카 최대의 쇼핑 지역입니다.\n걷는 것만으로도 트렌드와 일상의 활기가 동시에 전해지는 공간입니다.",
                    R.drawable.osaka_shinsaibashi
                ),
                s(
                    "난바",
                    "오사카 여행의 중심이 되는 지역으로 교통과 상업 시설이 밀집되어 있습니다.\n언제나 사람들로 붐비며 도시의 생동감을 가장 가까이에서 느낄 수 있습니다.",
                    R.drawable.osaka_namba
                )
            )),
            DayPlan(2, listOf(
                s(
                    "오사카성",
                    "도시 한가운데 웅장하게 자리한 성으로, 오사카의 역사와 상징을 한눈에 보여줍니다.\n넓은 공원을 따라 산책하며 과거와 현재가 공존하는 풍경을 즐기실 수 있습니다.",
                    R.drawable.osaka_castle
                ),
                s(
                    "우메다",
                    "고층 빌딩과 대형 쇼핑몰이 모여 있는 오사카의 현대적인 중심지입니다.\n전망대에서 내려다보는 도심 풍경은 여행의 흐름을 정리해주는 순간이 됩니다.",
                    R.drawable.osaka_umeda_sky
                ),
                s(
                    "우메다 스카이 빌딩",
                    "공중 정원 전망대로 유명한 랜드마크입니다.\n해 질 무렵부터 밤까지 이어지는 야경이 인상적인 장소입니다.",
                    R.drawable.osaka_umeda_sky
                )
            )),
            DayPlan(3, listOf(
                s(
                    "유니버설 스튜디오 재팬",
                    "영화와 캐릭터 세계를 하루 종일 즐길 수 있는 테마파크입니다.\n오사카 여행 일정 중 가장 역동적인 하루를 보내기에 좋은 장소입니다.",
                    R.drawable.osaka_usj
                ),
                s(
                    "덴덴타운",
                    "전자기기와 서브컬처 상점들이 모여 있는 개성 있는 거리입니다.\n오사카의 또 다른 취향과 문화를 가까이에서 만나실 수 있습니다.",
                    R.drawable.osaka_dendentown
                )
            ))
        )

        TripLength.D4_5 -> listOf(
            DayPlan(1, listOf(
                s(
                    "도톤보리",
                    "해가 지면 가장 화려해지는 오사카의 대표 거리입니다.\n여행의 시작을 생동감 있게 열어주는 첫날 저녁 장소로 잘 어울립니다.",
                    R.drawable.osaka_dotonbori
                ),
                s(
                    "신사이바시",
                    "쇼핑과 카페 문화가 자연스럽게 어우러진 거리입니다.\n천천히 걸으며 오사카의 일상을 느끼기 좋습니다.",
                    R.drawable.osaka_shinsaibashi
                )
            )),
            DayPlan(2, listOf(
                s(
                    "오사카성",
                    "오사카의 역사를 상징하는 대표적인 명소입니다.\n반나절 정도 여유 있게 둘러보며 산책하기 좋은 장소입니다.",
                    R.drawable.osaka_castle
                ),
                s(
                    "텐진바시스지 상점가",
                    "일본에서 가장 긴 상점가로, 소박한 로컬 분위기가 매력적인 곳입니다.\n현지인들의 일상을 가까이에서 느끼실 수 있습니다.",
                    R.drawable.osaka_tenjinbashi
                )
            )),
            DayPlan(3, listOf(
                s(
                    "유니버설 스튜디오 재팬",
                    "하루 일정으로 충분히 즐길 수 있는 대형 테마파크입니다.\n여행 중 가장 기억에 남는 하루를 만들어줍니다.",
                    R.drawable.osaka_usj
                )
            )),
            DayPlan(4, listOf(
                s(
                    "쿠로몬 시장",
                    "신선한 식재료와 길거리 음식이 가득한 오사카의 부엌입니다.\n먹거리 중심의 여행을 즐기기에 더없이 좋은 장소입니다.",
                    R.drawable.osaka_kuromon
                ),
                s(
                    "가이유칸",
                    "대형 수조가 인상적인 세계적인 규모의 수족관입니다.\n도심 여행 속에서 잠시 여유를 느끼기에 좋습니다.",
                    R.drawable.osaka_kaiyukan
                )
            )),
            DayPlan(5, listOf(
                s(
                    "우메다",
                    "여행의 마지막을 장식하기 좋은 쇼핑과 야경의 중심지입니다.\n도시의 불빛을 바라보며 오사카 여행을 차분히 마무리하실 수 있습니다.",
                    R.drawable.osaka_umeda_sky
                )
            ))
        )

        TripLength.D5_6 -> listOf(
            DayPlan(1, listOf(
                s(
                    "도톤보리",
                    "오사카 여행에서 빠질 수 없는 대표 거리입니다.\n첫날 저녁, 도시의 에너지를 가장 빠르게 느낄 수 있습니다.",
                    R.drawable.osaka_dotonbori
                )
            )),
            DayPlan(2, listOf(
                s(
                    "오사카성",
                    "오사카의 역사와 상징이 담긴 명소입니다.\n넓은 공원과 함께 여유로운 시간을 보내실 수 있습니다.",
                    R.drawable.osaka_castle
                )
            )),
            DayPlan(3, listOf(
                s(
                    "유니버설 스튜디오 재팬",
                    "하루를 온전히 보내기에 충분한 테마파크입니다.\n여행 중 가장 활동적인 일정이 됩니다.",
                    R.drawable.osaka_usj
                )
            )),
            DayPlan(4, listOf(
                s(
                    "우메다 스카이 빌딩",
                    "도시 전경을 한눈에 담을 수 있는 전망 명소입니다.\n오사카의 낮과 밤을 모두 감상하기에 좋습니다.",
                    R.drawable.osaka_umeda_sky
                )
            )),
            DayPlan(5, listOf(
                s(
                    "쿠로몬 시장",
                    "오사카의 식문화를 가장 가까이에서 느낄 수 있는 시장입니다.\n걷는 내내 음식 향기가 여행의 즐거움을 더해줍니다.",
                    R.drawable.osaka_kuromon
                )
            )),
            DayPlan(6, listOf(
                s(
                    "가이유칸",
                    "여행 후반부에 여유롭게 둘러보기 좋은 장소입니다.\n차분한 분위기 속에서 여행의 끝을 준비할 수 있습니다.",
                    R.drawable.osaka_kaiyukan
                )
            ))
        )
    }

    return TripPlan(city = City.OSAKA, length = length, days = days)
}
