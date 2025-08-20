# HrErp - İşe Alım Portalı

**HrErp**, şirketlerin işe alım süreçlerini yönetmesi için geliştirilmiş **mikroservis tabanlı** bir İnsan Kaynakları çözümüdür.  
Proje dört ana mikroservis üzerinden çalışır ve iş ilanlarından aday yönetimine kadar tüm süreci kapsar.

---

## ⚙️ Proje Modülleri

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

## 🛠️ Teknolojiler

- Java 17  
- Spring Boot  
- Spring Data JPA  
- Spring Cloud (Feign Client, Eureka, Config Server)  
- H2 / PostgreSQL  
- Lombok  
- Docker  

---

## 📌 Notlar

- HR rolü tüm CRUD işlemlerini gerçekleştirebilir.  
- Candidate rolü yalnızca ilanları görüntüleyebilir ve başvuru yapabilir.  
- Tüm mikroservisler Eureka Service Registry üzerinden haberleşir.  

---


