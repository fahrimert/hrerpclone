# HrErp - İşe Alım Portalı

**HrErp**, şirketlerin işe alım süreçlerini yönetmesi için geliştirilmiş **mikroservis tabanlı** bir İnsan Kaynakları çözümüdür.  
Proje dört ana mikroservis üzerinden çalışır ve iş ilanlarından aday yönetimine kadar tüm süreci kapsar.

---

##  Proje Modülleri

- **JobMS**  
  İş ilanlarının oluşturulması, listelenmesi, güncellenmesi, bunlara ait başvuruların alınması, güncellenmesi ve silinmesi işlemleri yapılır.

- **CandidateMS**  
  Aday listesinin alınması, aday kaydı, güncellenmesi, silinmesi ve aday bilgilerine erişim sağlanır.

- **RecruitmentMSFinal**  
  İşe alım süreçlerinin yönetimi yapılır; başvuruların takibi, aday değerlendirme ve süreç ilerletme bu modülde gerçekleşir.

- **Service Registry**  
  Mikroservislerin merkezi olarak kaydedildiği **Eureka tabanlı servis registry** bulunur.

---

##  Proje Akışı

- **Job Posting (HR)**  
  - HR kullanıcıları yeni iş ilanları oluşturabilir.  
  - Tüm ilanları veya spesifik bir ilanı görüntüleyebilir.  
  - İlanlar güncellenebilir veya silinebilir.  
  - İlanlara özel ID üzerinden erişim mümkündür.  

- **Candidate (Aday)**  
  - Adaylar kullanıcı hesabı oluşturabilir.  
  - HR tüm adayları veya tek bir adayı görüntüleyebilir.  
  - Adaylar kendi bilgilerini güncelleyebilir ve silebilir.  
  - Adayların teklif süreçleri bu servisten yönetilir.  

- **Application (Aday → İş İlanı)**  
  - Adaylar belirli bir iş ilanına başvurabilir.  
  - HR, iş ilanına başvuran tüm adayları veya spesifik adayları görüntüleyebilir.  
  - Başvuru statüleri güncellenebilir, uygun adaylar seçilebilir.  

- **Recruitment Process (İşe Alım Süreci)**  
  - HR, adaylara recruitment süreci başlatabilir.  
  - Süreç **HR Screening** ile başlar.  
  - HR adayları teknik mülakata yönlendirebilir veya reddedebilir.  
  - Case study, skorlar ve proper candidate seçimi yapılabilir.  
  - Adaylar kendi süreçlerini görüntüleyebilir.  

- **Service Registry (Eureka)**  
  - Tüm mikroservisler **Eureka** ile kaydedilmiştir.  
  - Registry `localhost:8761` üzerinde çalışır.  

---

##  JobPosting Service – Application Katmanı

JobPosting servisi, HR ve adayların iş ilanlarını yönetmesini ve görüntülemesini sağlar.  
Servis, aktif ilanları filtreleyerek sunar ve belirli bir iş ilanının detaylarını ID’ye göre getirme işlemlerini destekler.

---

##  JobPosting Model

JobPosting entity’si, veritabanında iş ilanlarını temsil eder.

- `id` → Benzersiz kimlik  
- `internalJobId` → Recruitment sürecinde kullanılacak iç ID  
- `jobTitle` → İş başlığı  
- `jobPostingDescription` → İş ilanı açıklaması  
- `internalDescripton` → HR tarafından kullanılan dahili açıklama  
- `salary` → Maaş bilgisi  
- `jobType` → İş tipi (enum)  
- `requiredSkillsList` → Gerekli yetkinlikler listesi  
- `department` → Departman  
- `hiringManagerName` → İlanı oluşturan HR ya da hiring manager  
- `applicationCount` → Başvuru sayısı  
- `location` → İşin lokasyonu  
- `postingStatus` → İlan durumu (enum)  
- `isReplacement` → İlanın mevcut çalışan yerine açılıp açılmadığı  
- `replacementFor` → Yerine geçilecek çalışan bilgisi (varsa)  
- `internalHrNote` → HR için dahili not  
- `jobPostingDeadline` → Son başvuru tarihi  
- `internalPostingDate` → İç sistemde ilan açılış tarihi  
- `jobPostingDate` → İlanın oluşturulma tarihi  

---

## API Endpointleri

###  Get All Job Postings
- **URL:** `/api/v1/jobPostings`  
- **Method:** `GET`  
- **Açıklama:** Sistemde yer alan tüm **aktif iş ilanlarını** listeler.  

###  Get Job Posting By ID
- **URL:** `/api/v1/jobPostings/{id}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir iş ilanını ID’sine göre getirir.  

###  Create Job Posting
- **URL:** `/api/v1/jobPostings`  
- **Method:** `POST`  
- **Açıklama:** Yeni bir iş ilanı oluşturur.  

###  Update Job Posting
- **URL:** `/api/v1/jobPostings/{id}`  
- **Method:** `PUT`  
- **Açıklama:** Mevcut bir iş ilanını günceller.  

###  Increment Application Count
- **URL:** `/api/v1/jobPostings/{id}/incrementApplication`  
- **Method:** `PUT`  
- **Açıklama:** İş ilanına yapılan başvuru sayısını artırır.  

###  Delete Job Posting
- **URL:** `/api/v1/jobPostings/{id}`  
- **Method:** `DELETE`  
- **Açıklama:** Belirli bir iş ilanını siler.  

###  Get Job Title
- **URL:** `/api/v1/jobPostings/{jobId}/getJobTitle`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir iş ilanının başlığını döndürür.  

###  Get Applications Based on Job
- **URL:** `/api/v1/jobPostings/{jobId}/getApplications`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir iş ilanına yapılan tüm başvuruları listeler.  

###  Get Single Application Based on Job
- **URL:** `/api/v1/jobPostings/{jobPostingId}/getApplication/{candidateId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir adayın, belirli bir ilana yaptığı başvuruyu getirir.  

###  Recruiter Specific Update on Job Posting
- **URL:** `/api/v1/jobPostings/{jobPostingId}/recruiterSpesificUpdate`  
- **Method:** `PUT`  
- **Açıklama:** Sadece recruiter’a özel alanları günceller.  

###  Recruiter Specific Fetch
- **URL:** `/api/v1/jobPostings/internal/{jobPostingId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir iş ilanına ait recruiter’a özel alanları döndürür.  

---
# Job Posting API Documentation

## Endpoints

---

### 🔹 Get All Job Postings
- **URL:** `/api/v1/jobPostings`
- **Method:** `GET`
- **Açıklama:** Sistemde yer alan tüm aktif iş ilanlarını listeler.

**Request:**  
- Bu endpoint herhangi bir parametre almaz.

**Response:**  
- Sistemdeki aktif ilanlar döndürülür.  
- Closed durumundaki ilanlar filtrelenir ve listelenmez.

**Özet:**  
- Tüm iş ilanlarını görüntülemek için kullanılır.  
- Hem HR hem de Candidate rolü tarafından erişilebilir.

---

### 🔹 Get Job Posting By ID
- **URL:** `/api/v1/jobPostings/{id}`
- **Method:** `GET`
- **Açıklama:** Belirli bir iş ilanını ID’sine göre getirir.

**Request:**  
- Path Variable:  
  - `id` → İş ilanının benzersiz kimliği.

**Response:**  
- Eğer ilan mevcutsa, ilan detayları döner.  
- Bulunamazsa, 404 Not Found hatası ve mesaj iletilir.

**Özet:**  
- Belirli bir iş ilanını ID’ye göre görüntülemek için kullanılır.  
- Hem HR hem de Candidate erişebilir.

---

### 🔹 Create Job Posting
- **URL:** `/api/v1/jobPostings`
- **Method:** `POST`
- **Açıklama:** Yeni bir iş ilanı oluşturur ve veritabanına kaydeder.

**Request Body:**  
- İş başlığı  
- İş tanımı  
- Maaş bilgisi  
- İş tipi  
- Gerekli yetkinlikler  
- Departman  
- Lokasyon  
- İlan durumu  
- Son başvuru tarihi  
- **Not:** Tüm alanlar zorunludur.

**Response:**  
- Başarılı oluşturma: HTTP 202 Accepted ve oluşturulan ilan başlığı.  
- Hata durumunda uygun HTTP hata kodu ve mesaj.

**Özet:**  
- Sadece HR rolü kullanabilir.  
- Oluşturulan ilan veritabanına kaydedilir ve listelenebilir.

---

### 🔹 Update Job Posting
- **URL:** `/api/v1/jobPostings/{id}`
- **Method:** `PUT`
- **Açıklama:** Mevcut bir iş ilanını ID’ye göre günceller.

**Request:**  
- Path Variable: `id` → Güncellenecek iş ilanının benzersiz kimliği.  
- Request Body: Güncellenecek iş ilanına ait bilgiler:  
  - İş başlığı  
  - İş tanımı  
  - Maaş bilgisi  
  - İş tipi  
  - Gerekli yetkinlikler  
  - Departman  
  - Lokasyon  
  - İlan durumu  
  - Son başvuru tarihi

**Response:**  
- Başarılı güncelleme: Güncellenmiş iş ilanının detayları döner.  
- Bulunamazsa uygun hata kodu ve mesaj.

**Özet:**  
- Sadece HR rolü kullanabilir.  
- Güncellenen ilan daha sonra ID ile erişilebilir ve listelenebilir.

---

### 🔹 Increment Application Count
- **URL:** `/api/v1/jobPostings/{id}/incrementApplication`
- **Method:** `PUT`
- **Açıklama:** Belirli bir iş ilanına yapılan başvuru sayısını artırır.

**Request:**  
- Path Variable: `id` → Başvuru sayısı artırılacak iş ilanının benzersiz kimliği.

**Response:**  
- İlan mevcutsa, başvuru sayısı bir artırılır ve başarı mesajı döner.  
- Bulunamazsa, 409 Conflict hatası ve uygun mesaj.

**Özet:**  
- Her yeni başvuru alındığında application count değerini güncellemek için kullanılır.  
- Hem HR hem de sistem tarafından tetiklenebilir.

---

### 🔹 Delete Job Posting
- **URL:** `/api/v1/jobPostings/{id}`
- **Method:** `DELETE`
- **Açıklama:** Belirli bir iş ilanını ID’sine göre siler.

**Request:**  
- Path Variable: `id` → Silinecek iş ilanının benzersiz kimliği.

**Response:**  
- İlan mevcutsa silinir ve başarı mesajı döner.  
- Bulunamazsa 409 Conflict hatası ve uygun mesaj.

**Özet:**  
- Sadece HR rolü kullanabilir.  
- Silinen ilan tekrar listelenemez veya güncellenemez.

---

### 🔹 Get Job Title
- **URL:** `/api/v1/jobPostings/{jobId}/getJobTitle`
- **Method:** `GET`
- **Açıklama:** Belirli bir iş ilanının başlığını ID’sine göre döndürür.

**Request:**  
- Path Variable: `jobId` → Başlığı alınacak iş ilanının benzersiz kimliği.

**Response:**  
- İlan mevcutsa iş başlığı döner.  
- Bulunamazsa `"Job does not exists"` mesajı iletilir.

**Özet:**  
- İş ilanının başlığına hızlı erişim sağlar.  
- Hem HR hem de sistem tarafından kullanılabilir.

---

### 🔹 Get Applications Based on Job
- **URL:** `/api/v1/jobPostings/{jobId}/getApplications`
- **Method:** `GET`
- **Açıklama:** Belirli bir iş ilanına yapılan başvuruları listeler.

**Request:**  
- Path Variable: `jobId` → Başvuruları alınacak iş ilanının benzersiz kimliği.

**Response:**  
- İlan mevcutsa: İş ilanı bilgileri ve başvuran adayların listesi döner.  
- Bulunamazsa 409 Conflict ve uygun mesaj.  
- Sunucu hatasında 500 Internal Server Error ve hata mesajı.

**Özet:**  
- HR, belirli bir iş ilanına yapılan tüm başvuruları görebilir.  
- Başvurular, aday bilgileri ile birlikte iş ilanı temel bilgilerini içerir.

---

### 🔹 Get Single Application Based on Job
- **URL:** `/api/v1/jobPostings/{jobPostingId}/getApplication/{candidateId}`
- **Method:** `GET`
- **Açıklama:** Belirli bir iş ilanına yapılan tek bir başvuruyu aday ID’si ile döndürür.

**Request:**  
- Path Variables:  
  - `jobPostingId` → Başvurusu alınacak iş ilanının ID’si  
  - `candidateId` → Başvurunun aday kimliği

**Response:**  
- İlan mevcutsa: İş ilanı bilgileri ve ilgili adayın başvuru detayları döner.  
- Bulunamazsa 409 Conflict ve uygun mesaj.

**Özet:**  
- HR, belirli bir iş ilanına ait tek bir aday başvurusunu görebilir.  
- Aday başvuru detayları iletişim ve sosyal medya bilgilerini içerir.

---

### 🔹 Recruiter Specific Update on Job Posting
- **URL:** `/api/v1/jobPostings/{jobPostingId}/recruiterSpesificUpdate`
- **Method:** `PUT`
- **Açıklama:** Belirli bir iş ilanında sadece recruiter’a özel alanları günceller.

**Request:**  
- Path Variable: `jobPostingId` → Güncellenecek iş ilanı  
- Request Body: Sadece recruiter’a özel alanlar:  
  - Internal Job ID  
  - Hiring Manager Name  
  - Internal HR Note  
  - Replacement bilgisi  
  - Internal Posting Date

**Response:**  
- İlan mevcutsa güncellenmiş bilgiler başarı mesajı ile döner.  
- Bulunamazsa veya Feign client hatası oluşursa, 409 Conflict ve mesaj.

**Özet:**  
- Sadece recruiter veya HR rolü kullanabilir.  
- İş ilanının genel alanları değişmez; sadece recruiter’a özel alanlar güncellenir.

---

### 🔹 Recruiter Specific Fetch
- **URL:** `/api/v1/jobPostings/internal/{jobPostingId}`
- **Method:** `GET`
- **Açıklama:** Belirli bir iş ilanına ait recruiter’a özel alanları getirir.

**Request:**  
- Path Variable: `jobPostingId` → Görüntülenecek iş ilanı

**Response:**  
- İlan mevcutsa, recruiter’a özel alanlar döner:  
  - Internal Job ID  
  - Hiring Manager Name  
  - Internal HR Note  
  - Replacement bilgisi  
  - Internal Posting Date  
- Bulunamazsa, 204 No Content ve mesaj.  
- Feign client veya sistem hatasında 409 Conflict ve hata mesajı.

**Özet:**  
- Sadece recruiter veya HR rolü kullanabilir.  
- İş ilanının genel bilgilerini değil, recruiter’a özel alanları döndürür.
# Candidate Service API Documentation

## Genel Açıklama
Candidate servisi, HR ve Candidate rollerinin aday bilgilerini yönetmesini sağlar.  
- Candidate, kendi profilini oluşturabilir, güncelleyebilir ve silebilir.  
- HR, tüm adayları veya belirli bir adayı görüntüleyebilir.  

Ayrıca Candidate servisi, Applications üzerinden iş ilanlarına yapılan başvuruların yönetiminde kritik rol oynar.

---

## Candidate Model
Candidate entity’si, veritabanında adayları temsil eder.

**Alanlar:**  
- `id` → Benzersiz kimlik  
- `firstName` → Adayın adı  
- `lastName` → Adayın soyadı  
- `address` → Adayın adres bilgileri (city, country, address)  
- `skills` → Adayın sahip olduğu yetkinlikler  
- `email` → Adayın e-posta adresi  
- `connections` → Adayın sosyal bağlantıları (LinkedIn, Instagram, Facebook, phoneNumber)  
- `cvUrl` → Adayın özgeçmiş linki  
- `createdAt` → Adayın oluşturulma tarihi  
- `applications` → Adayın yaptığı başvurular listesi  

---

## Applications Model
Adayların iş ilanlarına yaptıkları başvuruları temsil eder.

**Alanlar:**  
- `id` → Başvurunun benzersiz kimliği  
- `applicationDate` → Başvurunun yapıldığı tarih  
- `appliedPosition` → Başvurulan pozisyon adı  
- `coverLetter` → Adayın başvuru sırasında sunduğu ön yazı  
- `applicationStatus` → Başvurunun durumu (enum: APPLIED, INTERVIEW_SCHEDULED, REJECTED, OFFER_MADE)  
- `candidate` → Başvuruyu yapan aday  
- `jobPostingId` → Başvurunun yapıldığı iş ilanı kimliği  

---

## Address Model
Adayın adres bilgilerini içerir.  

**Alanlar:**  
- `city` → Şehir  
- `country` → Ülke  
- `address` → Açık adres  

---

## Connections Model
Adayın sosyal bağlantı ve iletişim bilgilerini içerir.  

**Alanlar:**  
- `linkedinUrl` → LinkedIn profili  
- `instagramUrl` → Instagram profili (opsiyonel)  
- `facebookUrl` → Facebook profili (opsiyonel)  
- `phoneNumber` → Telefon numarası (min. 10 karakter)  

---

## Kullanım Senaryosu
**Candidate:**  
- Sisteme kayıt olur (profil oluşturma)  
- Kendi profilini güncelleyebilir veya silebilir  
- JobPosting servisindeki ilanlara başvurabilir (Application üzerinden)

**HR:**  
- Tüm adayların listesini görüntüleyebilir  
- Belirli bir adayın detaylarını inceleyebilir  
- Adayların yaptığı başvuruları görebilir  

---

## API Endpoints

### 🔹 Create Application to Job Posting
- **URL:** `/api/v1/applications/createApplication/{jobPostingId}`  
- **Method:** `POST`  
- **Açıklama:** Adayın belirtilen iş ilanına başvuru yapmasını sağlar.

**Request:**  
- Path Parametreleri:  
  - `jobPostingId` → Başvuru yapılacak iş ilanının ID’si  
- Body (JSON): Adaya ait başvuru bilgileri

**Response:**  
- `success` → İşlem başarılı mı? (true | false)  
- `message` → Başvuru durumu hakkında bilgi  
- `data` → Kayıt edilen başvuru bilgileri  

**Özet:**  
- Candidate tarafından kullanılır  
- Başvuru başarılıysa başvuru kaydedilir ve iş ilanının başvuru sayısı artırılır  

---

### 🔹 Get All Applications Based on Job Posting
- **URL:** `/api/v1/applications/{jobId}/getApplications`  
- **Method:** `GET`  
- **Açıklama:** Belirtilen iş ilanına yapılan tüm başvuruları listeler.

**Request:**  
- Path Parametreleri:  
  - `jobId` → İş ilanının ID’si

**Response:**  
- `success`, `message`, `data` → Başvuruların listesi  
- Her başvuru objesi:  
  - `applicationId`, `applicationDate`, `candidateId`, `candidateFullName`, `candidateEmail`  

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Başvuran adayların temel bilgilerini ve başvuru tarihlerini listeler  

---

### 🔹 Get Candidate Application Detail by Job Posting
- **URL:** `/api/v1/applications/{jobId}/candidates/{candidateId}`  
- **Method:** `GET`  
- **Açıklama:** Adayın belirli bir iş ilanına yaptığı başvurunun tüm detaylarını getirir.

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Adayın iletişim bilgilerine, adresine, sosyal medya bağlantılarına ve CV’sine erişim sağlar  

---

### 🔹 Get Proper Candidates
- **URL:** `/api/v1/applications/{jobPostingId}/getTheProperCandidates`  
- **Method:** `GET`  
- **Açıklama:** İş ilanının gereksinimlerine uygun adayları listeler.

**Request:**  
- Path Parametreleri:  
  - `jobPostingId` → İş ilanı ID’si  

**Response:**  
- `success`, `message`, `data` → Uygun adayların listesi  
- Her aday objesi:  
  - `id`, `firstName`, `lastName`, `address`, `email`, `linkedin_url`, `skills`, `instagram_url`, `facebook_url`, `phoneNumber`, `cvUrl`, `createdAt`

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- İş ilanı gereksinimlerine uygun adayları otomatik filtreler  

---

### 🔹 Update Candidate’s Application Status
- **URL:** `/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus`  
- **Method:** `PUT`  
- **Açıklama:** Adayın başvuru durumunu günceller (örn. PENDING → APPROVED).

**Request:**  
- Path Parametreleri: `candidateId`  
- Body (JSON): Güncellenmek istenen başvuru durumu

**Response:**  
- `success`, `message`, `data` → Güncellenmiş başvuru durumu  

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Adayın başvuru sürecindeki durumu değiştirilir  

---

### 🔹 Get All Candidates
- **URL:** `/api/v1/candidates`  
- **Method:** `GET`  
- **Açıklama:** Sistemdeki tüm adayları listeler.

**Request:** Parametre yok  

**Response:**  
- `success`, `message`, `data` → Adayların listesi  
- Her aday objesi:  
  - `id`, `firstName`, `lastName`, `address`, `email`, `linkedinUrl`, `skills`, `instagramUrl`, `facebookUrl`, `phoneNumber`, `cvUrl`, `createdAt`  

---

### 🔹 Create Candidate
- **URL:** `/api/v1/candidates`  
- **Method:** `POST`  
- **Açıklama:** Yeni aday kaydı oluşturur.

**Request Body (CandidateRequestDTO):**  
- `firstName`, `lastName`, `address`, `email`, `linkedin_url`, `instagram_url`, `facebook_url`, `phoneNumber`, `cvUrl`, `createdAt`  

**Response (CandidateResponseDTO):**  
- `id`, `firstName`, `lastName`, `address`, `email`, `linkedinUrl`, `skills`, `instagramUrl`, `facebookUrl`, `phoneNumber`, `cvUrl`, `createdAt`  

---

### 🔹 Update Candidate
- **URL:** `/api/v1/candidates/{id}`  
- **Method:** `PUT`  
- **Açıklama:** Mevcut adayın bilgilerini günceller.

**Request:**  
- Path Parametre: `id`  
- Body: Güncellenmiş aday bilgileri (CandidateRequestDTO)

**Response (CandidateResponseDTO):**  
- Güncellenmiş aday bilgileri  

---

### 🔹 Delete Candidate
- **URL:** `/api/v1/candidates/{id}`  
- **Method:** `DELETE`  
- **Açıklama:** Adayı tamamen siler.

**Request:**  
- Path Parametre: `id`  

**Response:**  
- Standart ApiResponse ile işlem sonucu döner
# Recruitment Service – API Documentation

Recruitment servisi, HR ve adaylar arasındaki işe alım sürecini yönetir.  
Bu servis, **RecruitmentProcess** ve **Interview** yönetimini sağlar.

- HR, belirli bir Candidate için Recruitment Process başlatabilir.
- Süreç ilk olarak **HR Screening** aşaması ile başlar.
- Aday, süreç boyunca teknik mülakat, case project ve final görüşmelerden geçebilir.
- Her aşamada adayın performansı puanlanır ve süreç bir sonraki aşamaya taşınabilir veya **Rejected** durumuna alınabilir.
- Süreç sonunda **OfferStatus** belirlenerek iş teklifi yönetimi yapılır.

---

## Models

### RecruitmentProcess
Adayın işe alım sürecini temsil eder.

- `id` → Benzersiz kimlik  
- `candidateId` → Sürece dahil edilen adayın ID’si  
- `jobPostingId` → İlgili iş ilanının ID’si  
- `interviews` → Sürece bağlı mülakatların listesi  
- `interviewProcesses` → Mevcut süreç aşaması (enum: HR_SCREENING, TECHNICAL_INTERVIEW, CASE_PROJECT, FINAL_OVERVIEW, REJECTED)  
- `createdAt` → Sürecin oluşturulma tarihi  
- `lastUpdated` → Son güncelleme tarihi  

### Interview
Aday ile yapılan bir mülakatı temsil eder.

- `id` → Benzersiz kimlik  
- `candidateId` → Görüşülen adayın ID’si  
- `interviewRatingQuote` → Görüşme hakkındaki yorum  
- `interviewProcesses` → Görüşmenin türü (enum)  
- `interviewQuestions` → Sorular ve verilen yanıtlar  
- `interviewerName` → Görüşmeyi yapan kişi  
- `processSpecificData` → JSON formatında özel süreç verileri  
- `caseStudyProcesses` → Case study aşamasındaki durum  
- `interviewScore` → Görüşme puanı  
- `process` → İlgili recruitment process  
- `interviewScheduleTime` → Planlanan görüşme zamanı  
- `createdAt` → Oluşturulma tarihi  
- `lastUpdated` → Son güncelleme tarihi  

### InterviewQuestions
- `id` → Benzersiz kimlik  
- `questionText` → Soru metni  
- `candidateAnswer` → Adayın cevabı  
- `question_score` → Sorunun puanı  
- `interview` → Bağlı olduğu interview  
- `createdAt` → Oluşturulma tarihi  

---

## Enums

### InterviewProcesses
- HR_SCREENING  
- TECHNICAL_INTERVIEW  
- CASE_PROJECT  
- FINAL_OVERVIEW  
- REJECTED  

### InterviewScore
- BELOW_AVERAGE (0.0 - 2.5)  
- AVERAGE (2.5 - 5.0)  
- ABOVE_AVERAGE (5.0 - 7.5)  
- EXCELLENT (7.5 - 10.0)  

### OfferStatus
- OFFER_PENDING  
- OFFER_COUNTER_OFFER_CANDIDATE  
- OFFER_REJECTED_CANDIDATE  
- OFFER_ACCEPTED_RECRUITER  
- OFFER_COUNTER_OFFER_INTERNAL  
- OFFER_REJECTED_RECRUITER  

---

## API Endpoints

### Update Recruiter Specific Sections
- **URL:** `/api/v1/recruitment/{jobPostingId}/recruiterSpesificUpdate`  
- **Method:** `PUT`  
- **Açıklama:** İş ilanının recruiter/HR’e özel iç bölümlerini günceller.  
- **Request Body:** `JobPostingRequestRecruiterSpesificDTO`  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter  

---

### Get Job Posting (Internal)
- **URL:** `/api/v1/recruitment/internal/{jobPostingId}`  
- **Method:** `GET`  
- **Açıklama:** İş ilanının recruiter/HR’e özel iç detaylarını getirir.  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter  

---

### Get Proper Candidates
- **URL:** `/api/v1/recruitment/internal/getTheProperCandidates/{jobPostingId}`  
- **Method:** `GET`  
- **Açıklama:** İş ilanına uygun adayları listeler.  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter  

---

### Update Candidate Application Status
- **URL:** `/api/v1/recruitment/internal/updateTheCandidateApplicationStatus/{candidateId}`  
- **Method:** `PUT`  
- **Açıklama:** Adayın başvuru durumunu günceller.  
- **Request Body:** `ApplicationStatusUpdateDTO`  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter  

---

### Get Recruitment Processes for Candidate
- **URL:** `/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli adayın tüm işe alım süreçlerini getirir.  
- **Response:** `RecruitmentProcessInitiateResponseDTO` listesi  
- **Kullanıcı:** Candidate  

---

### Get Individual Recruitment Process
- **URL:** `/api/v1/recruitment/public/getTheInduvualRecruitmentProcesses/{candidateId}/{processId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir adayın spesifik işe alım sürecini getirir.  
- **Response:** `RecruitmentProcessInitiateResponseDTO`  
- **Kullanıcı:** Candidate  

---

### Initiate Recruitment Process
- **URL:** `/api/v1/recruitment/internal/initiateRecruitmentProcess`  
- **Method:** `POST`  
- **Açıklama:** Aday için yeni recruitment süreci başlatır (HR Screening ile).  
- **Request Body:** `RecruitmentProcessInitiateRequestDTO`  
- **Response:** `RecruitmentProcessInitiateResponseDTO`  
- **Kullanıcı:** HR/Recruiter  

---

### Reject Recruitment Process
- **URL:** `/api/v1/recruitment/internal/rejectRecruitmentProcess/{processId}`  
- **Method:** `POST`  
- **Açıklama:** Süreci **REJECTED** durumuna getirir ve mülakatları siler.  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter  

---

### Forward to Technical Interview Process
- **URL:** `/api/v1/recruitment/internal/{processId}/forwardToTheTechnicalInterviewProcess`  
- **Method:** `POST`  
- **Açıklama:** Süreci **TECHNICAL_INTERVIEW** aşamasına ilerletir ve teknik mülakat oluşturur.  
- **Request Body:** `InterviewTechnicalInterviewRequestDTO`  
- **Response:** `InterviewTechnicalResponseDTO`  
- **Kullanıcı:** HR/Recruiter/Technical Evaluator  

---

### Forward to Case Study Interview Process
- **URL:** `/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess`  
- **Method:** `POST`  
- **Açıklama:** Süreci **CASE_PROJECT** aşamasına ilerletir ve case study mülakatı oluşturur.  
- **Request Body:** `CaseStudyInterviewRequestDTO`  
- **Response:** `CaseStudyResponseDTO`  
- **Kullanıcı:** HR/Recruiter/Technical Evaluator  

---

### Initiate Case Study Interview Process
- **URL:** `/api/v1/recruitment/internal/{interviewId}/initiateTheCaseStudyInterviewProcess`  
- **Method:** `POST`  
- **Açıklama:** Case study mülakatını başlatır ve detaylarını kaydeder.  
- **Request Body:** `InitialCaseStudyInterviewDataDTO`  
- **Response:** `InitiateCaseStudyResponseDTO`  
- **Kullanıcı:** HR/Recruiter/Technical Evaluator  

---

### Solve Case Study Interview
- **URL:** `/api/v1/recruitment/internal/{interviewId}/solveTheCaseStudyInterview`  
- **Method:** `POST`  
- **Açıklama:** Case study çözümünü kaydeder ve süreci **SOLUTION** aşamasına günceller.  
- **Request Body:** `SolutionCaseStudyInterviewDTO`  
- **Response:** `SolutionCaseStudyResponseDTO`  
- **Kullanıcı:** HR/Recruiter/Technical Evaluator  

---

### Evaluate Case Study Interview
- **URL:** `/api/v1/recruitment/internal/{interviewId}/evaluateTheCaseStudyInterview`  
- **Method:** `POST`  
- **Açıklama:** Case study değerlendirmesini kaydeder ve süreci **EVALUATION** aşamasına günceller.  
- **Request Body:** `EvaluateCaseStudyInterviewDTO`  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR/Recruiter/Technical Evaluator  

# API Documentation – Get Candidate Averages on Job Posting
**Endpoint:** Get Candidate Averages on Job Posting  
**URL:** `/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting`  
**Method:** GET  
**Açıklama:** Belirli bir iş ilanı için tüm adayların tüm mülakat aşamalarındaki puanlarını ve genel ortalama puanlarını getirir.

### Request
**Path Parametreleri:**
- `jobPostingId` → Aday puanlarının getirileceği iş ilanının benzersiz ID'si (Positive Long)

### Response
**Response (JSON – FinalOverviewCandidateDTO[]):**  
İş ilanına başvuran tüm adayların detaylı puan bilgilerini içeren liste döner.

**FinalOverviewCandidateDTO Alanları:**
- `candidateResponseDTO` → Adayın kişisel ve iletişim bilgileri
- `HR_SCREENING` → HR ön değerlendirme puanı
- `TECHNICAL` → Teknik mülakat puanı
- `CASE_STUDY` → Case study mülakat puanı
- `INIT_CASE_STUDY` → Case study başlangıç puanı
- `EVALUATION_CASE_STUDY` → Case study değerlendirme puanı
- `AVERAGE_SCORE` → Tüm aşamaların ortalaması alınmış genel puan

**CandidateResponseDTO Alanları:**
- `id` → Adayın ID'si
- `firstName` → Adayın adı
- `lastName` → Adayın soyadı
- `address` → Adayın adres bilgileri
- `email` → Adayın e-posta adresi
- `linkedin_url` → LinkedIn profili URL'si
- `instagram_url` → Instagram profili URL'si
- `facebook_url` → Facebook profili URL'si
- `phoneNumber` → Telefon numarası
- `cvUrl` → CV dosyası bağlantısı
- `createdAt` → Oluşturulma tarihi

### Özet
Bu endpoint sadece HR/Recruiter tarafından kullanılır.  
Belirli bir iş ilanı için tüm adayların performans analizini yapmak için kullanılır.  
Bu endpoint:
1. İlan için tüm işe alım süreçlerini getirir
2. Her adayın tüm mülakat aşamalarındaki puanlarını çıkarır
3. Case study sürecindeki INIT ve EVALUATION puanlarını JSON verisinden ayıklar
4. Tüm puanların ortalamasını hesaplar
5. Adayın kişisel bilgileriyle birlikte detaylı puan raporu oluşturur

HR ekibi bu rapor sayesinde adayları genel performanslarına göre karşılaştırabilir ve nihai değerlendirme yapabilir.  
Eğer ilan için hiç aday bulunamazsa boş liste döndürülür.

---

# API Documentation – Create Offer to Candidate
**Endpoint:** Create Offer to Candidate  
**URL:** `/api/v1/recruitment/internal/createOfferOnSpesificCandidate`  
**Method:** POST  
**Açıklama:** Belirli bir adaya iş teklifi oluşturur ve teklif sürecini başlatır.

### Request
**Body (JSON – OfferRequestDTO):**
- `candidateId` → Teklif yapılacak adayın ID'si (Zorunlu)
- `internalJobId` → Şirket içi iş ID'si (Zorunlu)
- `jobPostingId` → İş ilanı ID'si (Zorunlu)
- `proposedSalary` → Önerilen maaş (Zorunlu, pozitif değer)
- `offerExpiryDate` → Teklifin son geçerlilik tarihi (Format: yyyy-MM-dd HH:mm)
- `candidateStartDate` → Adayın işe başlama tarihi (Format: yyyy-MM-dd HH:mm)
- `createdAt` → Oluşturulma tarihi (İsteğe bağlı, otomatik atanır)

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve oluşturulan teklifin detaylarını içerir.

**Alanlar:**
- `success` → İşlem başarılı mı? (true | false)
- `message` → İşlem hakkında bilgi mesajı
- `data` → Oluşturulan teklifin detayları (OfferResponseDTO)

**OfferResponseDTO Alanları:**
- `candidateResponseDTO` → Adayın kişisel ve iletişim bilgileri
- `proposedSalary` → Önerilen maaş
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `createdAt` → Teklif oluşturulma tarihi

### Özet
Bu endpoint sadece HR/Recruiter tarafından kullanılır.  
Bir adaya iş teklifi oluşturmak için kullanılır.  
Bu işlem:
1. Aynı aday ve iş ilanı için zaten bir teklif olup olmadığını kontrol eder
2. Adayın final overview aşamasını geçip geçmediğini kontrol eder
3. Yeni teklifi OFFER_PENDING durumuyla kaydeder
4. Adayın kişisel bilgileriyle birlikte teklif detaylarını döndürür

**Önemli Kontroller:**
- Aynı aday ve iş ilanı için zaten teklif varsa hata döner
- Aday final overview aşamasını geçmemişse teklif oluşturulamaz
- Tüm zorunlu alanların gönderilmesi gerekmektedir

Başarılı işlemde teklif detayları ve aday bilgileri response ile döndürülür.

---

# API Documentation – Get Individual Offer
**Endpoint:** Get Individual Offer  
**URL:** `/api/v1/recruitment/getOffer/{offerId}`  
**Method:** GET  
**Açıklama:** Belirli bir teklifin detaylı bilgilerini getirir.

### Request
**Path Parametreleri:**
- `offerId` → Detayları getirilecek teklifin benzersiz ID'si

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve teklifin detaylı bilgilerini içerir.

**Alanlar:**
- `success` → İşlem başarılı mı? (true | false)
- `message` → İşlem hakkında bilgi mesajı
- `data` → Teklifin detaylı bilgileri (OfferResponseWhenGetOfferDTO)

**OfferResponseWhenGetOfferDTO Alanları:**
- `offerStatus` → Teklifin durumu (OFFER_PENDING, OFFER_ACCEPTED, vb.)
- `proposedSalary` → Önerilen maaş
- `counterOfferSalaryCandidate` → Adayın karşı teklif maaşı (varsa)
- `counterOfferDemandsCandidate` → Adayın karşı teklif talepleri (varsa)
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `createdAt` → Sorgunun yapıldığı zaman

### Özet
Bu endpoint hem HR/Recruiter hem de Candidate tarafından kullanılabilir.  
Belirli bir teklifin güncel durumunu ve detaylarını görüntülemek için kullanılır.  
Bu endpoint:
1. Belirtilen ID'ye sahip teklifin mevcut olup olmadığını kontrol eder
2. Teklifin tüm detaylarını (teklif durumu, maaş bilgileri, karşı teklifler, son kullanma tarihi) getirir
3. Teklifin güncel durumunu gösterir

Teklif bulunamazsa uygun hata mesajı döndürülür.  
Başarılı işlemde teklifin tüm detayları response ile döndürülür.

---

# API Documentation – Get Candidate Offers
**Endpoint:** Get Candidate Offers  
**URL:** `/api/v1/recruitment/getOffers/{candidateId}`  
**Method:** GET  
**Açıklama:** Belirli bir adaya ait tüm iş tekliflerini listelemek için kullanılır.

### Request
**Path Parametreleri:**
- `candidateId` → Teklifleri getirilecek adayın benzersiz ID'si

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve adaya ait tüm tekliflerin listesini içerir.

**Alanlar:**
- `success` → İşlem başarılı mı? (true | false)
- `message` → İşlem hakkında bilgi mesajı
- `data` → Adaya ait tekliflerin listesi (OfferResponseWhenGetOfferDTO[])

**OfferResponseWhenGetOfferDTO Alanları:**
- `offerStatus` → Teklifin durumu (OFFER_PENDING, OFFER_ACCEPTED, vb.)
- `proposedSalary` → Önerilen maaş
- `counterOfferSalary` → Adayın karşı teklif maaşı (varsa)
- `counterOffDemands` → Adayın karşı teklif talepleri (varsa)
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `candidateStartDate` → Adayın işe başlama tarihi

### Özet
Bu endpoint hem HR/Recruiter hem de Candidate tarafından kullanılabilir.  
Bir adaya ait tüm iş tekliflerini görüntülemek için kullanılır.  
Bu endpoint:
1. Belirtilen aday ID'sine ait tüm teklifleri getirir
2. Her teklifin güncel durumunu ve detaylarını listeler
3. Tekliflerin son kullanma tarihlerini gösterir

Adaya ait hiç teklif bulunamazsa uygun hata mesajı döndürülür.  
Başarılı işlemde adaya ait tüm tekliflerin detaylı listesi response ile döndürülür.  
HR bu endpoint ile bir adayın tüm teklif geçmişini görüntüleyebilir, aday ise kendisine yapılan tüm teklifleri takip edebilir.

---

# API Documentation – Get Offers by Internal Job ID
**Endpoint:** Get Offers by Internal Job ID  
**URL:** `/api/v1/recruitment/getInduvualOfferForInternal/{internalJobId}`  
**Method:** GET  
**Açıklama:** Belirli bir şirket içi iş ID'sine ait tüm teklifleri listelemek için kullanılır.

### Request
**Path Parametreleri:**
- `internalJobId` → Teklifleri getirilecek şirket içi iş ID'si

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve şirket içi iş ID'sine ait tüm tekliflerin listesini içerir.

**Alanlar:**
- `success` → İşlem başarılı mı? (true | false)
- `message` → İşlem hakkında bilgi mesajı
- `data` → Şirket içi iş ID'sine ait tekliflerin listesi (OfferResponseWhenGetOfferDTO[])

**OfferResponseWhenGetOfferDTO Alanları:**
- `offerStatus` → Teklifin durumu (OFFER_PENDING, OFFER_ACCEPTED, vb.)
- `proposedSalary` → Önerilen maaş
- `counterOfferSalary` → Karşı teklif maaşı (varsa)
- `counterOffDemands` → Karşı teklif talepleri (varsa)
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `candidateStartDate` → Adayın işe başlama tarihi

### Özet
Bu endpoint sadece HR/Recruiter tarafından kullanılır.  
Belirli bir şirket içi iş ID'si için yapılan tüm teklifleri görüntülemek için kullanılır.  
Bu endpoint:
1. Belirtilen şirket içi iş ID'sine ait tüm teklifleri getirir
2. Her teklifin güncel durumunu ve detaylarını listeler
3. Aynı pozisyon için yapılan tüm teklifleri bir arada gösterir

Şirket içi iş ID'sine ait hiç teklif bulunamazsa uygun hata mesajı döndürülür.  
Başarılı işlemde ilgili pozisyona yapılan tüm tekliflerin detaylı listesi response ile döndürülür.  
HR bu endpoint ile belirli bir pozisyon için yapılan tüm teklifleri ve bunların durumlarını takip edebilir.

---

# API Documentation – Candidate Make Counter Offer
**Endpoint:** Candidate Make Counter Offer  
**URL:** `/api/v1/recruitment/candidateMakeCounterOffer/{offerId}`  
**Method:** PUT  
**Açıklama:** Adayın bir iş teklifine karşı teklif (counter offer) yapmasını sağlar.

### Request
**Path Parametreleri:**
- `offerId` → Karşı teklif yapılacak teklifin benzersiz ID'si

**Body (JSON – CounterOfferDTO):**
- `offerStatus` → Adayın teklif durum güncellemesi (örn: OFFER_COUNTER_OFFER_CANDIDATE)
- `counterOfferSalary` → Adayın karşı teklif maaşı (İsteğe bağlı)
- `counterOfferDemands` → Adayın karşı teklif talepleri (İsteğe bağlı)
- `role` → Adayın rolü/pozisyonu

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve güncellenen teklifin detaylarını içerir.

**CounterOfferResponseDTO Alanları:**
- `offerStatus` → Güncellenen teklif durumu
- `proposedSalary` → Orijinal önerilen maaş
- `counterOfferSalary` → Adayın karşı teklif maaşı
- `counterOffDemands` → Adayın karşı teklif talepleri
- `role` → Adayın rolü/pozisyonu
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `candidateStartDate` → Adayın işe başlama tarihi

### Özet
Bu endpoint sadece Candidate tarafından kullanılır.  
Bir adayın kendisine yapılan iş teklifine karşı teklif yapmasını sağlar.  
Bu işlem:
1. Belirtilen teklifin mevcut olup olmadığını kontrol eder
2. Adayın daha önce karşı teklif yapıp yapmadığını kontrol eder
3. Teklif durumunu, karşı teklif maaşını ve taleplerini günceller

**Önemli Kontroller:**
- Teklif bulunamazsa hata döner
- Aday zaten karşı teklif yapmışsa tekrar yapamaz
- Karşı teklif maaş ve talepleri isteğe bağlıdır

Başarılı işlemde güncellenen teklif detayları response ile döndürülür.  
Bu sayede aday, kendisine yapılan teklifi şirketin beklentilerine göre negotiate edebilir.

---

# API Documentation – Internal Make Counter Offer
**Endpoint:** Internal Make Counter Offer  
**URL:** `/api/v1/recruitment/internalMakeCounterOffer/{offerId}`  
**Method:** PUT  
**Açıklama:** HR/Recruiter'ın bir adayın karşı teklifine yanıt olarak şirket içi karşı teklif yapmasını sağlar.

### Request
**Path Parametreleri:**
- `offerId` → Karşı teklif yapılacak teklifin benzersiz ID'si

**Body (JSON – CounterOfferDTO):**
- `offerStatus` → Teklif durum güncellemesi (örn: OFFER_COUNTER_OFFER_INTERNAL)
- `counterOfferSalary` → Şirketin karşı teklif maaşı (İsteğe bağlı)
- `counterOfferDemands` → Şirketin karşı teklif talepleri (İsteğe bağlı)
- `role` → İlgili rol/pozisyon

### Response
**Response (JSON – ApiResponse):**  
İşlem sonucunu ve güncellenen teklifin detaylarını içerir.

**CounterOfferResponseDTO Alanları:**
- `offerStatus` → Güncellenen teklif durumu
- `proposedSalary` → Orijinal önerilen maaş
- `counterOfferSalary` → Şirketin karşı teklif maaşı
- `counterOffDemands` → Şirketin karşı teklif talepleri
- `role` → İlgili rol/pozisyon
- `offerExpiryDate` → Teklifin son geçerlilik tarihi
- `candidateStartDate` → Adayın işe başlama tarihi

### Özet
Bu endpoint sadece HR/Recruiter tarafından kullanılır.  
HR/Recruiter'ın bir adayın karşı teklifine yanıt olarak şirket içi karşı teklif yapmasını sağlar.  
Bu işlem:
1. Belirtilen teklifin mevcut olup olmadığını kontrol eder
2. Teklif durumunu, şirketin karşı teklif maaşını ve taleplerini günceller
3. Şirket içi karşı teklif bilgilerini kaydeder

**Önemli Not:**
- Karşı teklif maaş ve talepleri isteğe bağlıdır
- Bu endpoint, adayın karşı teklifine şirketin resmi yanıtını temsil eder
- Teklif durumu genellikle OFFER_COUNTER_OFFER_INTERNAL olarak güncellenir

Başarılı işlemde güncellenen teklif detayları response ile döndürülür.  
Bu sayede şirket ve aday arasındaki maaş müzakereleri resmi olarak yönetilebilir.


