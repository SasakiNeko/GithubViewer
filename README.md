# GithubViewer

## アプリに必要な機能
上から優先度順の機能

- リポジトリの検索
	- ソート機能(スター順、recentry)
- ソースコードを見る機能
- issueでのディスカッション機能
- アクションの通知を見れる機能

## アプリ概要
Githubのビューアー

## 使用ライブラリ
- 現状標準のJava/Kotlinライブラリ(android.xなど)

## アーキテクチャ
- クリーンアーキテクチャ
- MVVM

## ブランチ運用
- enhancement > feature/<issue番号>-<適当な名前>
- bug > fix/<issue番号>-<適当な名前>

## AndroidStudio
バージョン: 4.1.1

## ビルド方法について
アプリ内ではgithubトークンを使用することになるためトークンを生成し、
local.propertiesないにgithub_tokenというプロパティ名で保存すること。

```
// local.properties
例) github_token="ghp_000000111aaaabbbb"
```

※tokenの生成方法については以下を参照
https://docs.github.com/ja/graphql/guides/forming-calls-with-graphql#authenticating-with-graphql

## 計画シート(ガントチャート)
https://docs.google.com/spreadsheets/d/1Iyg9MqOpbCB-M9I8xl_eK_Hl_6-cenEaF3P9i4T_fzA/edit#gid=939584517
