# Sewol_ho_renewal
세월호 프로젝트입니다.

### 계기 :ship:

고등학교 1학년이었던 당시에 세월호 사건이 많았습니다. 그리고 진주에 노란 리본을 거는 행사를 보고, 참여까지 하고 싶었습니다. 하지만 당시 학교 문제와 학생이었던 신분인 저는 제대로 가지 못했습니다.
때문에 상당히 아쉬워했던 기억을 갖고 있었습니다. 그리고 어느 날 갑자기 문뜩 다른 사람들도 본인이 참여할 수 없다는 댓글을 보고는, 이 행사를 모티브로 하여, '어플리케이션으로 만들어보는 것은 어떨까?'
라는 생각을 하게되었고, 해당 어플리케이션을 계획하게 되었습니다.

### 기능 (Features.) :ship:

- 구글 로그인을 통한 간단한 로그인
- Firebase를 통한 실시간 게시판 글 처리
- 직관적인 디자인을 통한 알아보기 쉽게, 깔끔하게, 명확하게

----------------------------------------------------------------------------------------

### :speech_balloon: 사용 라이브러리 주소.
1. Firebase. https://firebase.google.com/ ( By Google library. )
2. Glide. https://github.com/bumptech/glide/ ( By Google library. )
3. Spotlight. https://github.com/29jitender/Spotlight ( By 29jitender. )

:speech_balloon: 구글 플레이 스토어 주소.
-> [ https://play.google.com/store/apps/details?id=com.solo_dev.remember_final ]

----------------------------------------------------------------------------------------

### :speech_balloon: 코드 업데이트 ( 최근 유지 보수 내역 ) in 2020.06.24

1. 안드로이드 패키지 변경. ( 기존에 실수가 있어서 해당 패키지를 변경 Refactor. )
2. 안드로이드 Data 클래스 코틀린화 ( String 이미지 키 값에 null을 통해서 firebase Child cannot empty or null. 와 같은 오류를 수정.
3. 차후 업데이트 예정.

----------------------------------------------------------------------------------------

in 2020.08.07

현재 설정 문제로 인해 구글 스토어 내의 앱에서 로그인이 되지 않는 현상이 발견되었으며, 수정중에 있습니다.

[ Solved on 2020.08.08 caused by : 로그인 토큰 SHA-1 키 값 토큰 오류 ]

----------------------------------------------------------------------------------------

in 2021.03.08

이미지가 없는 데이터베이스에도 이미지가 추가되는 현상이 있으며, 수정중에 있습니다.

[ Solved on 2021.03.09 caused by : 이미지 처리 추가 나중에 됨 ( 비동기 처리 ) ]

----------------------------------------------------------------------------------------
