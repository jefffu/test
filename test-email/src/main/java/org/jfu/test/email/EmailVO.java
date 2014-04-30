package org.jfu.test.email;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailVO {
    private final String fromName;

    private final List<String> to;
    private final List<String> cc;
    private final List<String> bcc;

    private final String subject;
    private final String text;
    private final String html;
    private final List<String> attachedFiles;
    private final List<Attachment> attachments;

    private EmailVO(Builder builder) {
        this.to = Collections.unmodifiableList(builder.to);
        if (builder.cc != null) {
            this.cc = Collections.unmodifiableList(builder.cc);
        } else {
            this.cc = null;
        }
        if (builder.bcc != null) {
            this.bcc = Collections.unmodifiableList(builder.bcc);
        } else {
            this.bcc = null;
        }
        this.subject = builder.subject;
        this.text = builder.text;
        this.html = builder.html;
        if (builder.attachedFiles != null) {
            this.attachedFiles = Collections.unmodifiableList(builder.attachedFiles);
        } else {
            this.attachedFiles = null;
        }
        if (builder.attachments != null) {
            this.attachments = Collections.unmodifiableList(builder.attachments);
        } else {
            this.attachments = null;
        }
        this.fromName = builder.fromName;
    }

    public Multipart getContent() throws MessagingException, IOException {
        Multipart multipartContent = new MimeMultipart("alternative");
        if (getHtml() != null) {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(getHtml(), "text/html;charset=utf8");
            multipartContent.addBodyPart(mbp);
        }
        if (getText() != null) {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(getText(), "utf8");
            multipartContent.addBodyPart(mbp);
        }
        if (!isEmpty(getAttachments()) || !isEmpty(getAttachedFiles())) {
            Multipart multipartContentWithAttachment = new MimeMultipart();
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setContent(multipartContent);
            multipartContentWithAttachment.addBodyPart(mbpContent);

            if (!isEmpty(getAttachedFiles())) {
                for (String file : getAttachedFiles()) {
                    MimeBodyPart mbpAttachment = new MimeBodyPart();
                    mbpAttachment.attachFile(file);
                    multipartContentWithAttachment.addBodyPart(mbpAttachment);
                }
            }

            if (!isEmpty(getAttachments())) {
                for (Attachment attachment : getAttachments()) {
                    MimeBodyPart mbpAttachment = new MimeBodyPart();
                    DataSource dataSource = new ByteArrayDataSource(
                            attachment.getContent(), FileTypeMap
                                    .getDefaultFileTypeMap().getContentType(
                                            attachment.getName()));
                    mbpAttachment.setDataHandler(new DataHandler(dataSource));
                    mbpAttachment.setFileName(attachment.getName());
                    multipartContentWithAttachment.addBodyPart(mbpAttachment);
                }
            }

            return multipartContentWithAttachment;
        }
        return multipartContent;
    }

    public String getFromName() {
        return fromName;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public List<String> getAttachedFiles() {
        return attachedFiles;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    private boolean isEmpty(Collection<?> col) {
        if (col == null) {
            return true;
        }
        return col.isEmpty();
    }

    public static class Builder {
        private List<String> to ;
        private List<String> cc;
        private List<String> bcc;

        private String fromName;

        private String subject;
        private String text;
        private String html;
        private List<String> attachedFiles;
        private List<Attachment> attachments;

        public Builder(String to, String subject, String text, String html) {
            assert to != null;
            assert subject != null;
            assert text != null || html != null;
            addTo(to);
            this.subject = subject;
            this.text = text;
            this.html = html;
        }

        public Builder addTo(String val) {
            if (to == null) {
                to = new LinkedList<String>();
            }
            to.add(val);
            return this;
        }

        public Builder addCc(String val) {
            if (cc == null) {
                cc = new LinkedList<String>();
            }
            cc.add(val);
            return this;
        }

        public Builder addBcc(String val) {
            if (bcc == null) {
                bcc = new LinkedList<String>();
            }
            bcc.add(val);
            return this;
        }

        public Builder setFromName(String val) {
            this.fromName = val;
            return this;
        }

        public Builder attachFile(String path) {
            if (attachedFiles == null) {
                attachedFiles = new LinkedList<String>();
            }
            attachedFiles.add(path);
            return this;
        }

        public Builder attach(String attachmentName, byte[] attachmentContent) {
            if (attachments == null) {
                attachments = new LinkedList<Attachment>();
            }
            attachments.add(new Attachment(attachmentName, attachmentContent));
            return this;
        }

        public EmailVO build() {
            return new EmailVO(this);
        }
    }

    public static class Attachment {
        private String name;
        private byte[] content;

        public Attachment(String name, byte[] content) {
            super();
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }
    }

}
