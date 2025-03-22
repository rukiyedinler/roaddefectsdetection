# Yol Bozukluğu Tespiti UI Projesi

Bu proje, yol bozukluğu tespiti ve bildirim süreçlerini yöneten bir kullanıcı arayüzü sağlar. Kullanıcılar, yol bozukluklarını tespit edip bildirebilir ve belediye çalışanları bu bozuklukları görüntüleyip detaylı bilgilere ulaşabilir.

## Özellikler

- **Kullanıcı Kayıt ve Giriş Ekranı:**
  - Kullanıcılar, e-posta ve şifre ile sisteme kayıt olabilir ve giriş yapabilir.
  - Şifre güvenliği, karmaşık karakterler ve uzunluk kontrolü gerektirir.
  - Kayıt sonrası kullanıcılar sisteme giriş yapabilir ve uygulamayı kullanmaya başlayabilir.

- **Uygulama Hakkında Bilgilendirme Ekranı:**
  - Kullanıcılara, uygulamanın mevcut özellikleri ve gelecekteki planları hakkında bilgi verilir.
  - Kullanıcılar, uygulamaya katkıda bulunma teşvikleriyle yönlendirilir.

- **Yol Bozukluğu Bildirim Ekranı:**
  - Kullanıcılar, yol bozukluğu tespit ettiklerinde fotoğraf çekerek bildirimde bulunabilirler.
  - Fotoğraf onaylanarak sistemde yer alır ve ilgili birimlere iletilir.

- **Yol Bozukluğu Listeleme Ekranı:**
  - Belediye çalışanları, vatandaşlar tarafından gönderilen yol bozukluğu fotoğraflarını listeleyebilir.
  - Yapay zeka destekli doğrulama ile, önceliklendirme işlemi daha hızlı ve objektif yapılır.

- **Yol Bozukluğu Detay Ekranı:**
  - Belediye çalışanları, her bir yol bozukluğu hakkında detaylı bilgi alabilir.
  - Görüntü, konum, doğruluk oranı ve ekleyen kullanıcı bilgileri gibi detaylar görüntülenir.

## Ekranlar ve Elemanlar

### 1. Kullanıcı Kayıt Ekranı
- **Amaç:** Kullanıcıların sisteme ilk kez kayıt olmalarını sağlamak.
- **Elemanlar:**
  - **Tam Ad:** Kullanıcının adını ve soyadını girmesi.
  - **E-posta:** Kullanıcının aktif e-posta adresi.
  - **Şifre:** Güvenlik amacıyla karmaşık şifre.
  - **Şifre Tekrar:** Şifrenin doğruluğunu kontrol etmek.
  - **Kayıt Ol:** Kullanıcıyı sisteme kaydeden buton.

### 2. Giriş Ekranı
- **Amaç:** Kullanıcıların sisteme giriş yapmasını sağlamak.
- **Elemanlar:**
  - **E-posta:** Sisteme kayıtlı e-posta adresi.
  - **Şifre:** Kullanıcının belirlediği şifre.
  - **Giriş Yap:** Giriş işlemi için buton.
  - **Yeni Kullanıcıysanız Kayıt Ol:** Kayıt olmayan kullanıcılar için link.

### 3. Uygulama Hakkında Bilgilendirme Ekranı
- **Amaç:** Kullanıcılara uygulamanın özellikleri ve gelecekteki planlar hakkında bilgi vermek.
- **Elemanlar:**
  - **Başlıklar:** "Şu An Ne Yapabiliyoruz?", "Gelecek Versiyonlarda Neler Olacak?", "Neden Katkıda Bulunmalısınız?"
  - **Metin:** Mevcut ve gelecekteki özellikler hakkında bilgiler.
  - **Görsel Elemanlar:** İçeriği destekleyen ikonlar.
  - **Çağrıya Yönelik Metinler:** Kullanıcıyı teşvik eden ifadeler.
  - **Düğme:** "Yol Bozukluğu Bildir" butonu.

### 4. Yol Bozukluğu Bildirim Ekranı
- **Amaç:** Kullanıcının yol bozukluğu fotoğrafını sisteme göndermesini sağlamak.
- **Elemanlar:**
  - **Görüntü:** Yol bozukluğu fotoğrafı.
  - **Gönder Butonu:** Fotoğrafı göndermek için buton.
  - **İptal Et Butonu:** Fotoğrafı iptal etmek için buton.
  - **Onay Mesajı:** Başarılı bildirim sonrası onay mesajı.
  - **Onay İkonu:** İşlem başarılı olduğunda gösterilen yeşil tik.
  - **Ana Sayfa Butonu:** Ana ekrana yönlendiren buton.

### 5. Yol Bozukluğu Listeleme Ekranı
- **Amaç:** Belediye çalışanlarının yol bozukluğu fotoğraflarını görüntülemesi.
- **Elemanlar:**
  - **Görüntüler:** Fotoğraflar veya harita görünümleri.
  - **Başlıklar:** Arama sonuçlarının başlıkları ve konum bilgisi.
  - **Yüzde Değerleri:** Alaka düzeyi yüzdesi.

### 6. Yol Bozukluğu Detay Ekranı
- **Amaç:** Yol bozukluğu hakkında daha detaylı bilgi sunmak.
- **Elemanlar:**
  - **Görüntü:** Yüksek çözünürlüklü fotoğraf.
  - **Konum Bilgisi:** Yol bozukluğunun tam adresi ve harita konumu.
  - **Doğruluk Oranı:** Yapay zekanın doğruluk oranı.
  - **Eklendiği Tarih:** Sisteme eklendiği tarih.
  - **Ekleyen Kullanıcı:** Yol bozukluğunu bildiren kullanıcı bilgisi.

## Proje Kurulumu

### Gereksinimler
- .NET 6 veya daha yeni bir sürüm.
- Visual Studio veya benzeri bir IDE.

### Proje Yapılandırması
- UI projesi, kullanıcı deneyimini en üst düzeye çıkarmayı hedefler.
- Veritabanı ve API bağlantıları için gerekli yapılandırmalar yapılmıştır.

### API Bağlantısı
Bu proje, API ile entegre çalışır ve yol bozukluğu verilerini sisteme aktarır. API kullanımı için [Road Defects Detection API](https://github.com/iclalucar/RoadDefectsApi.git) dokümantasyonuna göz atabilirsiniz.
