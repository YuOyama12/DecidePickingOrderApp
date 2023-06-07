# 生徒当て順番決めアプリ
![AppImage](https://user-images.githubusercontent.com/94959504/186296657-60c69f10-e1a9-49ea-bd23-45e5179da551.png)

Google Play ストアで公開中：[生徒当て順番決めアプリ](https://play.google.com/store/apps/details?id=com.yuoyama12.decidepickingorderapp)
## 概要
学校の先生が授業中等に生徒をランダムに指名する際に役に立つアプリです。

自分が教員であった頃の経験を基に「あったらうれしいアプリ」として作成してみました。
## 簡単な使い方紹介

1. 「グループ一覧」ボタンよりグループ・メンバー一覧画面へ移動

   ![screenShot01](https://user-images.githubusercontent.com/94959504/186300304-bc29cf10-1eb5-468f-9334-61d70717773e.PNG)
2. 「グループ新規作成」ボタンよりグループを作成した後、各項目にある「＋」ボタンよりグループ内のメンバーを追加。

   ![screenShot02](https://user-images.githubusercontent.com/94959504/186300307-eb35a1d7-6155-465c-842d-7343525149b0.PNG)
3. 最初の画面に戻り、「順番決め開始」ボタンより順番表示画面へ移動。フリック操作により表示されるメンバーを切り替えることが可能。（見やすさの観点から横向き表示のみ対応。）

   ![screenShot03](https://user-images.githubusercontent.com/94959504/186300308-bcfc0bf1-6e6b-4c7a-b26e-5fc3d7d6bdb4.PNG)

- 設定画面で、アプリをカスタマイズ可能。

   ![screenShot04](https://github.com/YuOyama12/DecidePickingOrderApp/assets/94959504/bcce1859-4838-421e-98e5-931e64f22a2e)

## 主な使用技術・ライブラリ等
- Kotlin
- Android Jetpack
  - Fragment
  - Room
  - viewModel
  - Navigation
  - Preferences
  - DataStore
  - View Binding
  - Data Binding
- SharedPreferences
- Gson
- Apache POI([license](https://www.apache.org/licenses/LICENSE-2.0))

## 動作対象端末
- Android8, 9, 10

## 搭載した機能
- 画面の向きがPortraitかLandscapeかで、レイアウトを一部変更し表示する機能
- リストを作成日時が古い順・新しい順等に並べ替える機能
- 設定画面を搭載し、その中で、フリック操作時の動く方向の設定をしたり順番表示画面での表示する項目の設定をしたりできる機能
- ある特定の人たちが次にくる場合にそれをお知らせしてくれる「直前通知カラー」設定機能
- 上記の「直前通知カラー」を目立たなくするために他に５色の色を設定できる機能
- スマホ内部に存在するExcelファイルからグループを作成してくれる機能
- 多言語対応（日本語・英語）

## こだわったポイント
### 機能面
- 上記にもあるように、設定画面を入れることで、ユーザの好きなように画面をカスタマイズすることができること。
### デザイン面
- ダークモードに対応したデザインを搭載したり、画面回転によってレイアウトが変わる機能を搭載したりしたこと。

## 今後やりたいこと
- さらにデザインをよくするために、画面遷移時のアニメーション等を活用したアプリの開発
- 他サービス等と連携した大規模アプリの開発