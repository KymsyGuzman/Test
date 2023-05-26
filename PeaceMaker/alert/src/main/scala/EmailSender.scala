package alert

import javax.mail._
import javax.mail.internet._
import java.util.Properties


object EmailSender {
  def sendEmail(to: String, subject: String, body: String): Unit = {
    val props = new Properties()
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "587") // Port 587 is used for TLS/STARTTLS
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")

    val session = Session.getInstance(props, new Authenticator() {
      //configurer l'@ mail et le mot de passe
      override protected def getPasswordAuthentication = new PasswordAuthentication("peaceland044@gmail.com", "peacelandtest123")
    })

    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress("peaceland044@gmail.com"))
      message.setRecipients(Message.RecipientType.TO, to) 
      //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
      //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Array(to)))
      message.setSubject(subject)
      message.setText(body)

      Transport.send(message)
      println(s"Email sent to $to with subject '$subject'")
    } catch {
      case e: MessagingException =>
        println(s"Failed to send email to $to with subject '$subject'")
        throw new RuntimeException(e)
    }
  }
}
