package com.gracecode.android.gojuon;


public interface Characters {
    // http://cmikavac.net/2012/09/08/mapping-japanese-kana--hiraganakatakana-for-use-in-programming/
    // data from http://cmikavac.net/download/kana.js

    // Each array element (representing sound) consists of 3 subelements
    // roumaji record, and hiragana and katakana symbols.
    public static final int INDEX_ROUMAJI = 0;
    public static final int INDEX_HIRAGANA = 1;
    public static final int INDEX_KATAKANA = 2;

    public static final String[][] MONOGRAPHS = {
            {"a", "あ", "ア"}, {"i", "い", "イ"}, {"u", "う", "ウ"}, {"e", "え", "エ"}, {"o", "お", "オ"},
            {"ka", "か", "カ"}, {"ki", "き", "キ"}, {"ku", "く", "ク"}, {"ke", "け", "ケ"}, {"ko", "こ", "コ"},
            {"sa", "さ", "サ"}, {"shi", "し", "シ"}, {"su", "す", "ス"}, {"se", "せ", "セ"}, {"so", "そ", "ソ"},
            {"ta", "た", "タ"}, {"chi", "ち", "チ"}, {"tsu", "つ", "ツ"}, {"te", "て", "テ"}, {"to", "と", "ト"},
            {"na", "な", "ナ"}, {"ni", "に", "ニ"}, {"nu", "ぬ", "ヌ"}, {"ne", "ね", "ネ"}, {"no", "の", "ノ"},
            {"ha", "は", "ハ"}, {"hi", "ひ", "ヒ"}, {"fu", "ふ", "フ"}, {"he", "へ", "ヘ"}, {"ho", "ほ", "ホ"},
            {"ma", "ま", "マ"}, {"mi", "み", "ミ"}, {"mu", "む", "ム"}, {"me", "め", "メ"}, {"mo", "も", "モ"},
            {"ya", "や", "ヤ"}, {"", "", ""}, {"yu", "ゆ", "ユ"}, {"", "", ""}, {"yo", "よ", "ヨ"},
            {"ra", "ら", "ラ"}, {"ri", "り", "リ"}, {"ru", "る", "ル"}, {"re", "れ", "レ"}, {"ro", "ろ", "ロ"},
            {"wa", "わ", "ワ"}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"o/wo", "を", "ヲ"},
            {"", "", ""}, {"", "", ""}, {"", "", ""}, {"", "", ""}, {"n", "ん", "ン"}
    };

    static final String[][] DIGRAPHS = {
            {"kya", "きゃ", "キャ"}, {"kyu", "きゅ", "キュ"}, {"kyo", "きょ", "キョ"},
            {"sha", "しゃ", "シャ"}, {"shu", "しゅ", "シュ"}, {"sho", "しょ", "ショ"},
            {"cha", "ちゃ", "チャ"}, {"chu", "ちゅ", "チュ"}, {"cho", "ちょ", "チョ"},
            {"nya", "にゃ", "ニャ"}, {"nyu", "にゅ", "ニュ"}, {"nyo", "にょ", "ニョ"},
            {"hya", "ひゃ", "ヒャ"}, {"hyu", "ひゅ", "ヒュ"}, {"hyo", "ひょ", "ヒョ"},
            {"mya", "みゃ", "ミャ"}, {"myu", "みゅ", "ミュ"}, {"myo", "みょ", "ミョ"},
            {"rya", "りゃ", "リャ"}, {"ryu", "りゅ", "リュ"}, {"ryo", "りょ", "リョ"}
    };

    static final String[][] MONOGRAPHS_WITH_DIACRITICS = {
            {"ga", "が", "ガ"}, {"gi", "ぎ", "ギ"}, {"gu", "ぐ", "グ"}, {"ge", "げ", "ゲ"}, {"go", "ご", "ゴ"},
            {"za", "ざ", "ザ"}, {"ji", "じ", "ジ"}, {"zu", "ず", "ズ"}, {"ze", "ぜ", "ゼ"}, {"zo", "ぞ", "ゾ"},
            {"da", "だ", "ダ"}, {"ji*", "ぢ", "ヂ"}, {"zu*", "づ", "ヅ"}, {"de", "で", "デ"}, {"do", "ど", "ド"},
            {"ba", "ば", "バ"}, {"bi", "び", "ビ"}, {"bu", "ぶ", "ブ"}, {"be", "べ", "ベ"}, {"bo", "ぼ", "ボ"},
            {"pa", "ぱ", "パ"}, {"pi", "ぴ", "ピ"}, {"pu", "ぷ", "プ"}, {"pe", "ぺ", "ペ"}, {"po", "ぽ", "ポ"}
    };

    static final String[][] DIGRAPHS_WITH_DIACRITICS = {
            {"gya", "ぎゃ", "ギャ"}, {"gyu", "ぎゅ", "ギュ"}, {"gyo", "ぎょ", "ギョ"},
            {"ja", "じゃ", "ジャ"}, {"ju", "じゅ", "ジュ"}, {"jo", "じょ", "ジョ"},
            {"ja*", "ぢゃ", "ヂャ"}, {"ju*", "ぢゅ", "ヂュ"}, {"jo*", "ぢょ", "ヂョ"},
            {"bya", "びゃ", "ビャ"}, {"byu", "びゅ", "ビュ"}, {"byo", "びょ", "ビョ"},
            {"pya", "ぴゃ", "ピャ"}, {"pyu", "ぴゅ", "ピュ"}, {"pyo", "ぴょ", "ピョ"}
    };

}
