<h1>SleepTight</h1>


<div>
  <table>
  <tr>
    <td align="center">
      문재혁
    </td>    
    <td align="center">
      임지민
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/Kiriiin">
        <img src="https://github.com/Kiriiin.png" width="80" alt="Kiriiin"/>
        <br/>
        <sub><b>Kiriiin</b></sub>
      </a>
      <br/>
    </td>
    <td align="center">
      <a href="https://github.com/jimini1026">
        <img src="https://github.com/jimini1026.png" width="80" alt="jimini1026"/>
        <br/>
        <sub><b>jimini1026</b></sub>
      </a>
      <br/>
    </td>
</table>
</div>


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/899269c5-fa4b-4e5f-9806-8a23c4d7cd7e)


### **어떻게 자야 잘 잤다고 소문이 날까?**

**Sleeptight은 사용자의 수면의 질을 높여주기**

**위해 만들어진 수면 관리 애플리케이션입니다.**

**최적의 수면 시간을 계산하여 알람을 맞추고,**

**자기 전 나만의 플레이 리스트를 재생하고,**

**한 달 간 수면 시간 패턴을 분석해보세요!**



![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/c21217ba-8341-4d43-8552-fed21e6fb395)

![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/351b2b51-c511-415a-a00a-6f3f20d18ebb)
![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/274dd897-457c-4e5f-b7be-0945b9783b60)


**로그인을 통해 사용자 정보를 불러옵니다**

- 카카오 로그인, 내부 로그인 중 하나를 골라 로그인합니다
- 로그인 성공 시, `Retrofit` 을 통해 서버와 통신하여 사용자 정보를 MainActivity로 전달
- 로그인 정보가 DB에 존재하지 않으면 자동으로 새 사용자 데이터를 서버에 추가


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/8768047f-2322-451b-80c1-7c3da3ad9572)
![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/c5c190ff-c4f8-41dc-86a5-f0fffb5f0c92)

**로그인을 통해 사용자 정보를 불러옵니다**

**취침 시간에 따른 최적 기상 시간을 추천해주고 알람을 설정할 수 있습니다**

- 취침 시간을 설정하면 RAM 수면 사이클에 맞추어 최적의 기상 시간을 추천해줍니다
- 원하는 기상 시간을 선택하면 `AlarmManagerCompat` 과 `NotificationCompat`을 통해 자동으로 알람이 맞추어집니다
- 기상 시간이 되어 알람이 울리며, 알람을 클릭하면 알람이 꺼지고 Wake Up 화면이 나옵니다
- 기상하기와 5분 후에 일어나기 중 선택할 수 있으며, 5분 후에 일어나기를 선택하면 자동으로 5분 후로 알람이 맞춰집니다
- 사용자가 최종으로 기상하기를 누르게 되면 취침 시간, 계획한 기상 시간, 실제 기상 시간이 데이터베이스에 저장됩니다


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/2c94d5ac-021e-471f-a98d-aa19c7685416)


**취침 전 편안한 음악을 들으며 수면의 질을 높일 수 있습니다**

- 기기 내장 오디오 트랙
    - `registerForActivityResult()` 함수를 통해 사용자에게 음악 접근 권한 요청 통해
        
        내장 오디오 트랙 접근
        
- 시간 설정 및 노래 재생
    - `RecyclerView` 를 이용하여 기기의 노래 목록 표시
    - `NumberPicker` 를 통해 시간 설정 가능
    - 노래 선택 후 save 버튼 → DB에 재생된 음악 데이터 저장
- 설정 시간이 끝나면 자동으로 재생이 종료됩니다.


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/c0ae97c3-db71-4fc7-9a7c-9c0ccc7a126e)
![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/0e20de74-9629-4c57-bda7-6ba8f84619bd)


**사용자의 지난 한 달 간 수면 관련 정보들을 보여줍니다**

- 지난 한 달 간 사용자의 취침 시간, 목표 기상 시간, 실제 기상 시간을 데이터베이스에서 불러와 표시해 줍니다
- 지난 한 달 간 사용자의 수면 데이터를 데이터베이스에서 불러와  한 달 간의 평균 취침 시간, 기상 시간, 수면 시간을 계산하여 나타냅니다
- 사용자가 취침 전 가장 많이 들었던 노래 Top3 와 각각의 재생 횟수를 데이터베이스에서 불러와 보여줍니다

**사용자의 프로필을 보여줍니다**

- History Tab에서 한 번 더 스와이프하면 `DrawerLayout`을 통해 프로필 탭이 표시가 됩니다.
- 사용자의 이름, ID, 최근 들은 음악들을 데이터베이스에서 불러와 보여줍니다
- ID의 경우 kakao 로그인은 ‘kakao_@’ 형식으로 표시가 되며, 내장 로그인의 경우 사용자가 설정한 ID가 표시됩니다


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/015b4498-f693-4dc2-b92b-3a819c2d0dbc)


**USERS : PRIMARY KEY {user_id}**

- user_id : user의 아이디를 저장합니다. Kakao 로그인의 경우 ‘kakao_#’의 형태로 카카오 ID를 저장하고, 내부 로그인의 경우 사용자가 지정한 ID를 저장합니다
- user_name : user의 이름을 저장합니다. Kako 로그인의 경우 사용자의 이름을 저장하고, 내부 로그인의 경우 사용자가 지정한 이름을 저장합니다

**USER_SLEEP_DATA : PRIMARY KEY {user_id, date}**

- user_id : USERS의 user_id를 참조하는 Foreign Key
- data : user가 알람을 설정한 날짜를 저장합니다
- sleeptime : user가 설정한 취침 시간을 저장합니다
- pred_waketime : user가 선택한 목표 기상 시간을 저장합니다
- real_waketime : user가 실제로 알람을 끄고 일어난 시간을 저장합니다

**USER_SONGS_DATA : PRIMARY KEY {user_id, song}**

- user_id : USERS의 user_id를 참조하는 Foreign Key
- song : user가 재생목록에 추가하고 재생한 음악을 저장합니다
- play_num : user가 노래를 재생한 횟수를 저장합니다


![image](https://github.com/jimini1026/Kaist_Week2_Sleeptight_front/assets/128363259/ad97f99b-55dc-42e0-acec-93fdf067a4d0)

<h3>Release</h3>
https://drive.google.com/file/d/1lxvAbOzi0G0kRGo8L_0gXKnSUl8jpYaN/view?usp=sharing
