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

- **Servis Registry**  
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

- **Servis Registry (Eureka)**  
  - Tüm mikroservisler **Eureka** ile kaydedilmiştir.  
  - Registry `localhost:8761` üzerinde çalışır.  

---

##  JobPosting Servis – Application Katmanı

JobPosting servisi, HR ve adayların iş ilanlarını yönetmesini ve görüntülemesini sağlar.  
Servis, aktif ilanları filtreleyerek sunar ve belirli bir iş ilanının detaylarını ID’ye göre getirme işlemlerini destekler.

---

##  JobPosting Model

JobPosting entity’si, veritabanında iş ilanlarını temsil eder.

- `id` ,
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
# Job Posting API Dökümantasyonu

## Endpoints

---

###  Get All Job Postings
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

###  Get Job Posting By ID
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

###  Create Job Posting
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

###  Update Job Posting
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

###  Increment Application Count
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

###  Delete Job Posting
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

###  Get Job Title
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

###  Get Applications Based on Job
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

###  Get Single Application Based on Job
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

###  Recruiter Specific Update on Job Posting
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

###  Recruiter Specific Fetch
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
# Candidate Servis API Dökümantasyonu

## Genel Açıklama
Candidate servisi, HR ve Candidate rollerinin aday bilgilerini yönetmesini sağlar.  
- Candidate, kendi profilini oluşturabilir, güncelleyebilir ve silebilir.  
- HR, tüm adayları veya belirli bir adayı görüntüleyebilir.  

Ayrıca Candidate servisi, Applications üzerinden iş ilanlarına yapılan başvuruların yönetiminde kritik rol oynar.

---

## Candidate Model
Candidate entity’si, veritabanında adayları temsil eder.

**Alanlar:**  
- `id` ,
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

## API Endpoints

###  Create Application to Job Posting
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

###  Get All Applications Based on Job Posting
- **URL:** `/api/v1/applications/{jobId}/getApplications`  
- **Method:** `GET`  
- **Açıklama:** Belirtilen iş ilanına yapılan tüm başvuruları listeler.

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Başvuran adayların temel bilgilerini ve başvuru tarihlerini listeler  

---

###  Get Candidate Application Detail by Job Posting
- **URL:** `/api/v1/applications/{jobId}/candidates/{candidateId}`  
- **Method:** `GET`  
- **Açıklama:** Adayın belirli bir iş ilanına yaptığı başvurunun tüm detaylarını getirir.

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Adayın iletişim bilgilerine, adresine, sosyal medya bağlantılarına ve CV’sine erişim sağlar  

---

###  Get Proper Candidates
- **URL:** `/api/v1/applications/{jobPostingId}/getTheProperCandidates`  
- **Method:** `GET`  
- **Açıklama:** İş ilanının gereksinimlerine uygun adayları listeler.

**Request:**  
- Path Parametreleri:  
  - `jobPostingId` → İş ilanı ID’si  

**Response:**  
-  Uygun adayların listesi  
- Her aday objesi:  
  - `id`, `firstName`, `lastName`, `address`, `email`, `linkedin_url`, `skills`, `instagram_url`, `facebook_url`, `phoneNumber`, `cvUrl`, `createdAt`

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- İş ilanı gereksinimlerine uygun adayları otomatik filtreler  

---

###  Update Candidate’s Application Status
- **URL:** `/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus`  
- **Method:** `PUT`  
- **Açıklama:** Adayın başvuru durumunu günceller (örn. PENDING → APPROVED).

**Request:**  
- Path Parametreleri: `candidateId`  
- Body (JSON): Güncellenmek istenen başvuru durumu

**Response:**  
-  Güncellenmiş başvuru durumu  

**Özet:**  
- HR veya Recruiter tarafından kullanılır  
- Adayın başvuru sürecindeki durumu değiştirilir  

---

###  Get All Candidates
- **URL:** `/api/v1/candidates`  
- **Method:** `GET`  
- **Açıklama:** Sistemdeki tüm adayları listeler.

---

###  Create Candidate
- **URL:** `/api/v1/candidates`  
- **Method:** `POST`  
- **Açıklama:** Yeni aday kaydı oluşturur.

**Request Body (CandidateRequestDTO):**  
- `firstName`, `lastName`, `address`, `email`, `linkedin_url`, `instagram_url`, `facebook_url`, `phoneNumber`, `cvUrl`, `createdAt`  

**Response (CandidateResponseDTO):**  
- `id`, `firstName`, `lastName`, `address`, `email`, `linkedinUrl`, `skills`, `instagramUrl`, `facebookUrl`, `phoneNumber`, `cvUrl`, `createdAt`  

---

###  Update Candidate
- **URL:** `/api/v1/candidates/{id}`  
- **Method:** `PUT`  
- **Açıklama:** Mevcut adayın bilgilerini günceller.

**Request:**  
- Path Parametre: `id`  
- Body: Güncellenmiş aday bilgileri (CandidateRequestDTO)

**Response (CandidateResponseDTO):**  
- Güncellenmiş aday bilgileri  

---

###  Delete Candidate
- **URL:** `/api/v1/candidates/{id}`  
- **Method:** `DELETE`  
- **Açıklama:** Adayı tamamen siler.

**Request:**  
- Path Parametre: `id`  

**Response:**  
- Standart ApiResponse ile işlem sonucu döner
# Recruitment Servis – API Dökümantasyonu

Recruitment servisi, HR ve adaylar arasındaki işe alım sürecini yönetir.  
Bu servis, **RecruitmentProcess** ve **Interview** yönetimini sağlar.

- HR, belirli bir Candidate için Recruitment Process başlatabilir.
- Süreç ilk olarak **HR Screening** aşaması ile başlar.
- Aday, süreç boyunca teknik mülakat, case project ve final görüşmelerden geçebilir.
- Her aşamada adayın performansı puanlanır ve süreç bir sonraki aşamaya taşınabilir veya **Rejected** durumuna alınabilir.
- Süreç sonunda **OfferStatus** belirlenerek iş teklifi yönetimi yapılır.

---

## Modeller

### RecruitmentProcess
Adayın işe alım sürecini temsil eder.

- `id` ,
- `candidateId` → Sürece dahil edilen adayın ID’si  
- `jobPostingId` → İlgili iş ilanının ID’si  
- `interviews` → Sürece bağlı mülakatların listesi  
- `interviewProcesses` → Mevcut süreç aşaması (enum: HR_SCREENING, TECHNICAL_INTERVIEW, CASE_PROJECT, FINAL_OVERVIEW, REJECTED)  
- `createdAt` → Sürecin oluşturulma tarihi  
- `lastUpdated` → Son güncelleme tarihi  

### Interview
Aday ile yapılan bir mülakatı temsil eder.

- `id` ,
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
- `id` ,
- `questionText` → Soru metni  
- `candidateAnswer` → Adayın cevabı  
- `question_score` → Sorunun puanı  
- `interview` → Bağlı olduğu interview  
- `createdAt` → Oluşturulma tarihi  

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

### Get Candidate Averages on Job Posting
- **URL:** `/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir iş ilanı için tüm adayların mülakat puanlarını ve ortalama puanlarını getirir.  
- **Request Body:** Yok  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR / Recruiter  

---

### Create Offer to Candidate
- **URL:** `/api/v1/recruitment/internal/createOfferOnSpesificCandidate`  
- **Method:** `POST`  
- **Açıklama:** Belirli bir adaya iş teklifi oluşturur ve teklif sürecini başlatır.  
- **Request Body:** `OfferRequestDTO`  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR / Recruiter  

---

### Get Individual Offer
- **URL:** `/api/v1/recruitment/getOffer/{offerId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir teklifin detaylı bilgilerini getirir.  
- **Request Body:** Yok  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR / Recruiter / Candidate  

---

### Get Candidate Offers
- **URL:** `/api/v1/recruitment/getOffers/{candidateId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir adaya ait tüm iş tekliflerini getirir.  
- **Request Body:** Yok  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR / Recruiter / Candidate  

---

### Get Offers by Internal Job ID
- **URL:** `/api/v1/recruitment/getInduvualOfferForInternal/{internalJobId}`  
- **Method:** `GET`  
- **Açıklama:** Belirli bir şirket içi iş ID’sine ait tüm teklifleri getirir.  
- **Request Body:** Yok  
- **Response:** `ApiResponse`  
- **Kullanıcı:** HR / Recruiter  

---

### Candidate Make Counter Offer
- **URL:** `/api/v1/recruitment/candidateMakeCounterOffer/{offerId}`  
- **Method:** `PUT`  
- **Açıklama:** Adayın iş teklifine karşı teklif yapmasını sağlar.  
- **Request Body:** `CounterOfferDTO`  
- **Response:** `ApiResponse`  
- **Kullanıcı:** Candidate  

# Test Dökümantasyonu 

Tüm endpointler için **Controller Unit Test**, **Servis Test** ve **Integration Test**ler yapılmıştır.  
Feign client ve proxy tabanlı entegrasyonlar için controller unit testleri yeterli kapsamı sağlamaktadır.  

**Test Araçları:**
- **Controller Unit Test:** `@WebMvcTest`, `MockMvc`, `@MockitoBean`
- **Servis Test:** `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`
- **Integration Test:** `@SpringBootTest`, `@AutoConfigureMockMvc`, `@Transactional`, `MockMvc`

---

##  Candidate Endpoints

| Method | URL | Açıklama | Test Durumu |
|--------|-----|----------|-------------|
| POST | `/api/v1/candidates` | Yeni aday oluştur | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/candidates/{id}` | Aday detaylarını getir | Controller Unit Test, Servis Test, Integration Test |
| PUT | `/api/v1/candidates/{id}` | Aday bilgilerini güncelle | Controller Unit Test, Servis Test, Integration Test |
| DELETE | `/api/v1/candidates/{id}` | Adayı sil | Controller Unit Test, Servis Test |
| GET | `/api/v1/candidates` | Tüm adayları listele | Controller Unit Test, Integration Test |
| GET | `/api/v1/candidates/existsById/{id}` | Adayın var olup olmadığını kontrol et | Controller Unit Test |
| GET | `/api/v1/candidates/{candidateId}/getMyOffers` | Adayın tekliflerini getir | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/candidates/{offerId}/getInduvualOffer` | Spesifik teklifi getir | Controller Unit Test, Servis Test |
| PUT | `/api/v1/candidates/candidateMakeCounterOffer/{offerId}` | Aday karşı teklif yap | Controller Unit Test, Servis Test |

---

##  Application Endpoints

| Method | URL | Açıklama | Test Durumu |
|--------|-----|----------|-------------|
| POST | `/api/v1/applications/createApplication/{jobPostingId}` | İlana başvuru oluştur | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/applications/{jobId}/getApplications` | İlanın başvurularını getir | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/applications/{jobPostingId}/getApplication/{candidateId}` | Adayın ilana başvurusunu getir | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/applications/{jobPostingId}/getTheProperCandidates` | Uygun adayları listele | Controller Unit Test |
| PUT | `/api/v1/applications/{candidateId}/updateTheCandidateApplicationStatus` | Başvuru durumunu güncelle | Controller Unit Test, Servis Test |

---

##  Job Posting Endpoints

| Method | URL | Açıklama | Test Durumu |
|--------|-----|----------|-------------|
| GET | `/api/v1/jobPostings` | Tüm iş ilanlarını listele | Controller Unit Test, Integration Test |
| GET | `/api/v1/jobPostings/{id}` | İlan detaylarını getir | Controller Unit Test, Integration Test |
| POST | `/api/v1/jobPostings` | Yeni iş ilanı oluştur | Controller Unit Test, Servis Test |
| PUT | `/api/v1/jobPostings/{id}` | İlan bilgilerini güncelle | Controller Unit Test, Servis Test |
| PUT | `/api/v1/jobPostings/{id}/incrementApplication` | Başvuru sayısını artır | Controller Unit Test, Servis Test |
| DELETE | `/api/v1/jobPostings/{id}` | İlanı sil | Controller Unit Test, Servis Test |
| GET | `/api/v1/jobPostings/{jobId}/getJobTitle` | İlan başlığını getir | Controller Unit Test, Servis Test |
| GET | `/api/v1/jobPostings/{jobId}/getApplications` | İlanın başvurularını getir | Controller Unit Test, Servis Test |
| GET | `/api/v1/jobPostings/{jobPostingId}/getApplication/{candidateId}` | Adayın başvurusunu getir | Controller Unit Test, Servis Test |
| PUT | `/api/v1/jobPostings/{jobPostingId}/recruiterSpesificUpdate` | İlanın recruiter bölümlerini güncelle | Controller Unit Test, Servis Test |
| GET | `/api/v1/jobPostings/internal/{jobPostingId}` | İlanın iç detaylarını getir | Controller Unit Test |
| GET | `/api/v1/jobPostings/existsById/{id}` | İlanın var olup olmadığını kontrol et | Controller Unit Test |

---

##  Recruitment Process Endpoints

| Method | URL | Açıklama | Test Durumu |
|--------|-----|----------|-------------|
| PUT | `/api/v1/recruitment/{jobPostingId}/recruiterSpesificUpdate` | İlanın recruiter bölümlerini güncelle | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/internal/{jobPostingId}` | İlanın iç detaylarını getir | Controller Unit Test |
| GET | `/api/v1/recruitment/internal/getTheProperCandidates/{jobPostingId}` | İlan için uygun adayları listele | Controller Unit Test |
| PUT | `/api/v1/recruitment/internal/updateTheCandidateApplicationStatus/{candidateId}` | Adayın başvuru durumunu güncelle | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/public/getTheRecruitmentProcesses/{candidateId}` | Adayın tüm süreçlerini getir | Controller Unit Test, Servis Test, Integration Test |
| GET | `/api/v1/recruitment/public/getTheInduvualRecruitmentProcess/{candidateId}/{processId}` | Spesifik süreç detaylarını getir | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/initiateRecruitmentProcess` | Yeni işe alım süreci başlat | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/rejectRecruitmentProcess/{processId}` | Süreci reddet | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/{processId}/forwardToTheTechnicalInterviewProcess` | Teknik mülakat aşamasına ilerlet | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/{processId}/forwardToTheCaseStudyInterviewProcess` | Case study aşamasına ilerlet | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/{interviewId}/initiateTheCaseStudyInterview` | Case study mülakatını başlat | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/public/{interviewId}/solveTheCaseStudyInterview` | Case study çözümünü kaydet | Controller Unit Test, Servis Test, Integration Test |
| POST | `/api/v1/recruitment/internal/{interviewId}/evaluateTheCaseStudyInterview` | Case study değerlendirmesini kaydet | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/internal/{jobPostingId}/getCandidateAveragesOnJobPosting` | İlandaki aday ortalamalarını getir | Controller Unit Test, Servis Test |
| POST | `/api/v1/recruitment/internal/changeProcessToFinalOverview/{candidateId}/{processId}` | Süreci final aşamaya taşı | Controller Unit Test, Servis Test |

---

##  Offer Endpoints

| Method | URL | Açıklama | Test Durumu |
|--------|-----|----------|-------------|
| POST | `/api/v1/recruitment/internal/createOfferOnSpesificCandidate` | Adaya teklif oluştur | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/getOffer/{offerId}` | Teklif detaylarını getir | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/getOffers/{candidateId}` | Adayın tekliflerini listele | Controller Unit Test, Servis Test |
| GET | `/api/v1/recruitment/getInduvualOfferForInternal/{internalJobId}` | Şirket içi iş ID'sine göre teklifleri getir | Controller Unit Test, Servis Test |
| PUT | `/api/v1/recruitment/candidateMakeCounterOffer/{offerId}` | Aday karşı teklif yap | Controller Unit Test, Servis Test |
| PUT | `/api/v1/recruitment/internalMakeCounterOffer/{offerId}` | Şirket karşı teklif yap | Controller Unit Test, Servis Test |



